package com.corkili.learningserver.service.impl;

import com.corkili.learningserver.bo.CourseCatalog;
import com.corkili.learningserver.bo.Scorm;
import com.corkili.learningserver.bo.User;
import com.corkili.learningserver.common.ScormZipUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.repo.ScormRepository;
import com.corkili.learningserver.scorm.SCORM;
import com.corkili.learningserver.scorm.SCORMResult;
import com.corkili.learningserver.scorm.cam.load.SCORMPackageManager;
import com.corkili.learningserver.scorm.cam.model.ContentPackage;
import com.corkili.learningserver.scorm.cam.model.DeliveryContent;
import com.corkili.learningserver.scorm.common.ID;
import com.corkili.learningserver.scorm.rte.api.SCORMRuntimeManager;
import com.corkili.learningserver.scorm.sn.api.SCORMSeqNavManager;
import com.corkili.learningserver.scorm.sn.api.event.NavigationEvent;
import com.corkili.learningserver.service.ScormService;
import com.corkili.learningserver.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional
public class ScormServiceImpl extends ServiceImpl<Scorm, com.corkili.learningserver.po.Scorm> implements ScormService {

    @Autowired
    private ScormRepository scormRepository;

    @Autowired
    private UserService userService;

    private SCORM scormManager;

    private SCORMPackageManager scormPackageManager;

    private SCORMRuntimeManager scormRuntimeManager;

    private SCORMSeqNavManager scormSeqNavManager;

    private Map<String, DeliveryContent> deliveryContentMap;

    public ScormServiceImpl() {
        scormManager = SCORM.getInstance();
        scormPackageManager = scormManager.getPackageManager();
        scormRuntimeManager = scormManager.getRuntimeManager();
        scormSeqNavManager = scormManager.getSnManager();
        deliveryContentMap = new HashMap<>();
    }

    @Override
    protected JpaRepository<com.corkili.learningserver.po.Scorm, Long> repository() {
        return scormRepository;
    }

    @Override
    protected String entityName() {
        return "scorm";
    }

    @Override
    protected Scorm newBusinessObject() {
        return new Scorm();
    }

    @Override
    protected com.corkili.learningserver.po.Scorm newPersistObject() {
        return new com.corkili.learningserver.po.Scorm();
    }

    @Override
    protected Logger logger() {
        return log;
    }

    @Override
    public ServiceResult importScorm(Scorm scorm, byte[] scormZipData) {
        if (StringUtils.isBlank(scorm.getPath())) {
            return recordErrorAndCreateFailResultWithMessage("import scorm error: scorm path is empty");
        }
        if (!ScormZipUtils.storeScormZip(scorm.getPath(), scormZipData)) {
            return recordErrorAndCreateFailResultWithMessage("import scorm error: store scorm zip in filesystem failed");
        }
        Optional<Scorm> scormOptional = create(scorm);
        if (!scormOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage("import scorm error: store scorm in db failed");
        }
        scorm = scormOptional.get();
        // validate
        ContentPackage contentPackage = scormPackageManager.launch(String.valueOf(scorm.getId()), true);
        if (contentPackage == null) {
            scormPackageManager.unlaunch(String.valueOf(scorm.getId()));
            deleteScorm(scorm.getId());
            return recordErrorAndCreateFailResultWithMessage("import scorm error: scorm format invalid");
        }
        scormPackageManager.unlaunch(String.valueOf(scorm.getId()));
        return ServiceResult.successResult("import scorm success", Scorm.class, scorm);
    }

    @Override
    public ServiceResult deleteScorm(Long scormId) {
        retrieve(scormId).ifPresent(scorm -> ScormZipUtils.deleteScormZip(scorm.getPath()));
        if (!delete(scormId)) {
            return recordWarnAndCreateSuccessResultWithMessage("delete scorm success");
        }
        return ServiceResult.successResultWithMesage("delete scorm success");
    }

    @Override
    public ServiceResult queryCatalog(Long scormId) {
        if (scormId == null || !scormRepository.existsById(scormId)) {
            return recordErrorAndCreateFailResultWithMessage("query catalog error: scorm [{}] not exist", scormId);
        }
        ContentPackage contentPackage = scormPackageManager.launch(String.valueOf(scormId));
        if (contentPackage == null) {
            return recordErrorAndCreateFailResultWithMessage("query catalog error: launch scorm package failed");
        }
        CourseCatalog courseCatalog = CourseCatalog.generateFromContentPackage(contentPackage);
        if (courseCatalog == null) {
            return recordErrorAndCreateFailResultWithMessage("query catalog error: generate course catalog failed");
        }
        return ServiceResult.successResult("query catalog success", CourseCatalog.class, courseCatalog);
    }

    @Override
    public ServiceResult processNavigationEvent(NavigationEvent navigationEvent, Long userId, Long scormId, String level1CatalogItemId) {
        Optional<User> userOptional = userService.retrieve(userId);
        if (userId == null || !userOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage("process navigation event error: user [{}] not exist", userId);
        }
        if (scormId == null || !scormRepository.existsById(scormId)) {
            return recordErrorAndCreateFailResultWithMessage("process navigation event error: scorm [{}] not exist", scormId);
        }
        User user = userOptional.get();
        if (!scormSeqNavManager.launch(String.valueOf(scormId), user, level1CatalogItemId)) {
            return recordErrorAndCreateFailResultWithMessage("process navigation event error: launch sn data error");
        }
        SCORMResult scormResult = scormManager.process(navigationEvent, new ID(level1CatalogItemId, String.valueOf(scormId), String.valueOf(user.getId())));
        if (!scormResult.isSuccess()) {
            return recordErrorAndCreateFailResultWithMessage("process navigation event error: {}", scormResult.getErrorMsg());
        }
        DeliveryContent deliveryContent = scormResult.getDeliveryContent();
        if (deliveryContent != null && scormResult.getDeliveryActivity() != null) {
            deliveryContentMap.put(ServiceUtils.format("{}-{}-{}", userId, scormId, deliveryContent.getItemId()), deliveryContent);
            if (!scormRuntimeManager.launch(String.valueOf(scormId), deliveryContent.getItemId(), user, true)) {
                return recordErrorAndCreateFailResultWithMessage("process navigation event error: launch runtime data error");
            }
            if (!scormManager.mapTrackingInfoToRuntimeData(scormResult.getDeliveryActivity())) {
                return recordErrorAndCreateFailResultWithMessage("process navigation event error: init runtime data error");
            }
        }
        return ServiceResult.successResult("process navigation event success", DeliveryContent.class, deliveryContent);
    }

    @Override
    public ServiceResult invokeLMSRuntimeAPI(Long userId, Long scormId, String itemId, String methodName, String parameter1, String parameter2) {
        Optional<User> userOptional = userService.retrieve(userId);
        if (userId == null || !userOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage("invoke runtime api [{}({})({})] error: user [{}] not exist", methodName, parameter1, parameter2, userId);
        }
        if (scormId == null || !scormRepository.existsById(scormId)) {
            return recordErrorAndCreateFailResultWithMessage("invoke runtime api [{}({})({})] error: scorm [{}] not exist", methodName, parameter1, parameter2, scormId);
        }
        ID attemptID = new ID(itemId, String.valueOf(scormId), String.valueOf(userId));
        String result;
        if ("initialize".equalsIgnoreCase(methodName)) {
            result = scormRuntimeManager.initialize(attemptID, parameter1);
        } else if ("terminate".equalsIgnoreCase(methodName)) {
            result = scormRuntimeManager.terminate(attemptID, parameter1);
        } else if ("getValue".equalsIgnoreCase(methodName)) {
            result = scormRuntimeManager.getValue(attemptID, parameter1);
        } else if ("setValue".equalsIgnoreCase(methodName)) {
            result = scormRuntimeManager.setValue(attemptID, parameter1, parameter2);
        } else if ("commit".equalsIgnoreCase(methodName)) {
            result = scormRuntimeManager.commit(attemptID, parameter1);
        } else if ("getLastError".equalsIgnoreCase(methodName)) {
            result = scormRuntimeManager.getLastErrorCode(attemptID);
        } else {
            return recordErrorAndCreateFailResultWithMessage("invoke runtime api [{}({})({})] error: method not undefined", methodName, parameter1, parameter2);
        }
        return recordErrorAndCreateFailResultWithMessage("invoke runtime api [{}({})({})] success", methodName,
                parameter1, parameter2, String.class, result);
    }

    @Override
    public DeliveryContent getDeliveryContent(Long userId, Long scormId, String itemId) {
        return deliveryContentMap.get(ServiceUtils.format("{}-{}-{}", userId, scormId, itemId));
    }
}
