package com.corkili.learningserver.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.CourseCatalog;
import com.corkili.learningserver.bo.CourseCatalog.CourseCatalogItem;
import com.corkili.learningserver.bo.Scorm;
import com.corkili.learningserver.bo.User;
import com.corkili.learningserver.common.ScormZipUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.po.ScormData;
import com.corkili.learningserver.po.ScormRecord;
import com.corkili.learningserver.repo.ScormDataRepository;
import com.corkili.learningserver.repo.ScormRecordRepository;
import com.corkili.learningserver.repo.ScormRepository;
import com.corkili.learningserver.scorm.SCORM;
import com.corkili.learningserver.scorm.SCORMResult;
import com.corkili.learningserver.scorm.cam.load.SCORMPackageManager;
import com.corkili.learningserver.scorm.cam.model.ContentPackage;
import com.corkili.learningserver.scorm.cam.model.DeliveryContent;
import com.corkili.learningserver.scorm.common.ID;
import com.corkili.learningserver.scorm.common.LMSPersistDriver;
import com.corkili.learningserver.scorm.common.LMSPersistDriverManager;
import com.corkili.learningserver.scorm.rte.api.LearnerAttempt;
import com.corkili.learningserver.scorm.rte.api.SCORMRuntimeManager;
import com.corkili.learningserver.scorm.rte.model.RuntimeData;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;
import com.corkili.learningserver.scorm.sn.api.AttemptManager;
import com.corkili.learningserver.scorm.sn.api.SCORMSeqNavManager;
import com.corkili.learningserver.scorm.sn.api.event.EventType;
import com.corkili.learningserver.scorm.sn.api.event.NavigationEvent;
import com.corkili.learningserver.scorm.sn.model.tree.Activity;
import com.corkili.learningserver.service.ScormService;
import com.corkili.learningserver.service.UserService;

@Slf4j
@Service
@Transactional
public class ScormServiceImpl extends ServiceImpl<Scorm, com.corkili.learningserver.po.Scorm> implements ScormService, LMSPersistDriver {

    @Autowired
    private ScormRepository scormRepository;

    @Autowired
    private ScormDataRepository scormDataRepository;

    @Autowired
    private ScormRecordRepository scormRecordRepository;

    @Autowired
    private UserService userService;

    private SCORM scormManager;

    private SCORMPackageManager scormPackageManager;

    private SCORMRuntimeManager scormRuntimeManager;

    private SCORMSeqNavManager scormSeqNavManager;

    private Map<String, DeliveryContent> deliveryContentMap;

    public ScormServiceImpl() {
        LMSPersistDriverManager.getInstance().registerDriver(this);
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
    public ServiceResult queryCatalog(Long scormId, Long userId) {
        if (scormId == null || !scormRepository.existsById(scormId)) {
            return recordErrorAndCreateFailResultWithMessage("query catalog error: scorm [{}] not exist", scormId);
        }
        Optional<User> userOptional = userService.retrieve(userId);
        if (userId == null || !userOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage("query catalog error: user [{}] not exist", userId);
        }
        User user = userOptional.get();
        ContentPackage contentPackage = scormPackageManager.launch(String.valueOf(scormId));
        if (contentPackage == null) {
            return recordErrorAndCreateFailResultWithMessage("query catalog error: launch scorm package failed");
        }
        CourseCatalog courseCatalog = CourseCatalog.generateFromContentPackage(contentPackage);
        if (courseCatalog == null) {
            return recordErrorAndCreateFailResultWithMessage("query catalog error: generate course catalog failed");
        }
        List<CourseCatalogItem> level1ItemList = courseCatalog.getItems().get(1);
        if (level1ItemList != null) {
            for (CourseCatalogItem courseCatalogItem : level1ItemList) {
                scormSeqNavManager.launch(String.valueOf(scormId), user, courseCatalogItem.getItemId());
            }
        }
        List<CourseCatalogItem> leafItemList = courseCatalog.getItems().get(courseCatalog.getMaxLevel());
        if (leafItemList != null) {
            for (CourseCatalogItem courseCatalogItem : leafItemList) {
                scormRuntimeManager.launch(String.valueOf(scormId), courseCatalogItem.getItemId(), user);
                LearnerAttempt learnerAttempt = scormRuntimeManager.getLearnerAttempt(
                        new ID(courseCatalogItem.getItemId(), String.valueOf(scormId), String.valueOf(user.getId())));
                RuntimeData runtimeData = learnerAttempt.getRuntimeData();
                BigDecimal ctValue = runtimeData.getCmi().getCompletionThreshold().getCompletionThreshold().getValue();
                if (ctValue != null) {
                    courseCatalogItem.setCompletionThreshold(ctValue.doubleValue());
                } else {
                    courseCatalogItem.setCompletionThreshold(null);
                }
                BigDecimal pmValue = runtimeData.getCmi().getProgressMeasure().getProgressMeasure().getValue();
                if (pmValue != null) {
                    courseCatalogItem.setProgressMeasure(pmValue.doubleValue());
                } else {
                    courseCatalogItem.setProgressMeasure(0.0);
                }
                ScormResult scormResult = runtimeData.getCmi().getCompletionStatus().get();
                if (scormResult.getError() != ScormError.E_0 || "".equals(scormResult.getReturnValue())) {
                    courseCatalogItem.setCompletionStatus("unknown");
                } else {
                    // "completed", "incomplete", "not_attempted", "unknown"
                    courseCatalogItem.setCompletionStatus(scormResult.getReturnValue());
                }
            }
        }
        if (level1ItemList != null) {
            for (CourseCatalogItem courseCatalogItem : level1ItemList) {
                scormSeqNavManager.forceMapRuntimeData2TrackingInfo(new ID(courseCatalogItem.getItemId(),
                        String.valueOf(scormId), String.valueOf(userId)));
            }
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
        ID snID = new ID(level1CatalogItemId, String.valueOf(scormId), String.valueOf(user.getId()));
        AttemptManager attemptManager = scormSeqNavManager.findAttemptManagerBy(snID);
        Activity oldActivity = null;
        if (attemptManager != null) {
            oldActivity = attemptManager.getTargetActivityTree().getGlobalStateInformation().getCurrentActivity();
        }
        NavigationEvent processedNavigationEvent = navigationEvent;
        if (navigationEvent.getType() == EventType.Start) {
            Optional<ScormRecord> scormRecordOptional = scormRecordRepository.findByScormIdAndLearnerId(scormId, userId);
            if (scormRecordOptional.isPresent()) {
                ScormRecord scormRecord = scormRecordOptional.get();
                if (StringUtils.isNotBlank(scormRecord.getLastItem())) {
                    processedNavigationEvent = new NavigationEvent(EventType.Choose, scormRecord.getLastItem());
                }
            }
        }
        SCORMResult scormResult = scormManager.process(processedNavigationEvent, snID);
        if (!scormResult.isSuccess()) {
            if (processedNavigationEvent.getType() == EventType.Choose && navigationEvent.getType() == EventType.Start) {
                // reset runtime and retry start
                if (!scormSeqNavManager.launch(String.valueOf(scormId), user, level1CatalogItemId, true)) {
                    return recordErrorAndCreateFailResultWithMessage("process navigation event error: {}", scormResult.getErrorMsg());
                }
                processedNavigationEvent = navigationEvent;
                scormResult = scormManager.process(processedNavigationEvent, snID);
            }
        }
        if (navigationEvent.getType() == EventType.ExitAll || navigationEvent.getType() == EventType.AbandonAll) {
            scormRuntimeManager.unlaunch(user, String.valueOf(scormId));
            scormSeqNavManager.unlaunch(String.valueOf(userId), String.valueOf(scormId));
            String prefix = ServiceUtils.format("{}-{}", userId, scormId);
            List<String> shouldRemoveContentKeyList = new LinkedList<>();
            deliveryContentMap.keySet().forEach(k -> {
                if (k.startsWith(prefix)) {
                    shouldRemoveContentKeyList.add(k);
                }
            });
            for (String key : shouldRemoveContentKeyList) {
                deliveryContentMap.remove(key);
            }
        }
        if (navigationEvent.getType() == EventType.ExitAll || navigationEvent.getType() == EventType.AbandonAll
                || navigationEvent.getType() == EventType.UnqualifiedExit) {
            if (oldActivity != null) {
                Optional<ScormRecord> scormRecordOptional = scormRecordRepository.findByScormIdAndLearnerId(scormId, userId);
                ScormRecord scormRecord;
                if (scormRecordOptional.isPresent()) {
                    scormRecord = scormRecordOptional.get();
                } else {
                    scormRecord = new ScormRecord();
                    com.corkili.learningserver.po.Scorm scormPO = new com.corkili.learningserver.po.Scorm();
                    scormPO.setId(scormId);
                    scormRecord.setScorm(scormPO);
                    com.corkili.learningserver.po.User userPO = new com.corkili.learningserver.po.User();
                    user.setId(userId);
                    scormRecord.setLearner(userPO);
                    scormRecord.setCreateTime(new Date());
                }
                scormRecord.setUpdateTime(new Date());
                scormRecord.setLastItem(oldActivity.getId().getIdentifier());
                scormRecordRepository.save(scormRecord);
                deliveryContentMap.remove(ServiceUtils.format("{}-{}-{}", user, scormId, oldActivity.getId().getIdentifier()));
            }
        }
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
        return ServiceResult.successResult(ServiceUtils.format("invoke runtime api [{}({})({})] success",
                methodName, parameter1, parameter2), String.class, result);
    }

    @Override
    public DeliveryContent getDeliveryContent(Long userId, Long scormId, String itemId) {
        return deliveryContentMap.get(ServiceUtils.format("{}-{}-{}", userId, scormId, itemId));
    }

    @Override
    public void onLogout(Long userId) {
        User user = userService.retrieve(userId).orElse(null);
        if (user != null) {
            scormRuntimeManager.unlaunch(user);
            scormSeqNavManager.unlaunch(user);
            String uid = String.valueOf(userId);
            List<String> shouldRemoveContentKeyList = new LinkedList<>();
            deliveryContentMap.keySet().forEach(k -> {
                if (k.startsWith(uid)) {
                    shouldRemoveContentKeyList.add(k);
                }
            });
            for (String key : shouldRemoveContentKeyList) {
                deliveryContentMap.remove(key);
            }
        }
    }

    @Override
    public String querySCORMPackageZipFilePathBy(String lmsContentPackageID) {
        Long scormID = Long.valueOf(lmsContentPackageID);
        Scorm scorm = retrieve(scormID).orElse(null);
        if (scorm == null) {
            return null;
        } else {
            return ScormZipUtils.getScormZipPath(scorm.getPath());
        }
    }

    @Override
    public int queryActivityAttemptCountBy(String lmsContentPackageID, String activityID, String learnerID) {
        List<ScormData> scormDataList = scormDataRepository.findAllByScormIdAndItemAndLearnerId(
                Long.valueOf(lmsContentPackageID), activityID, Long.valueOf(learnerID));
        if (scormDataList == null || scormDataList.size() != 1) {
            return 0;
        }
        ScormData scormData = scormDataList.get(0);
        return scormData.getAttemptCnt();
    }

    @Override
    public void saveActivityAttemptCount(String lmsContentPackageID, String activityID, String learnerID, int attemptCount) {
        List<ScormData> scormDataList = scormDataRepository.findAllByScormIdAndItemAndLearnerId(
                Long.valueOf(lmsContentPackageID), activityID, Long.valueOf(learnerID));
        ScormData scormData;
        if (scormDataList == null || scormDataList.size() != 1) {
            scormData = new ScormData();
            scormData.setCreateTime(new Date());
            com.corkili.learningserver.po.Scorm scorm = new com.corkili.learningserver.po.Scorm();
            scorm.setId(Long.valueOf(lmsContentPackageID));
            scormData.setScorm(scorm);
            scormData.setItem(activityID);
            com.corkili.learningserver.po.User user = new com.corkili.learningserver.po.User();
            user.setId(Long.valueOf(learnerID));
            scormData.setLearner(user);
        } else {
            scormData = scormDataList.get(0);
        }
        scormData.setUpdateTime(new Date());
        scormData.setAttemptCnt(attemptCount);
        scormDataRepository.save(scormData);
    }

    @Override
    public String queryRuntimeDataBy(String lmsContentPackageID, String activityID, String learnerID) {
        List<ScormData> scormDataList = scormDataRepository.findAllByScormIdAndItemAndLearnerId(
                Long.valueOf(lmsContentPackageID), activityID, Long.valueOf(learnerID));
        if (scormDataList == null || scormDataList.size() != 1) {
            return "";
        }
        ScormData scormData = scormDataList.get(0);
        return scormData.getRuntimeData();
    }

    @Override
    public void saveRuntimeData(String lmsContentPackageID, String activityID, String learnerID, String runtimeData) {
        List<ScormData> scormDataList = scormDataRepository.findAllByScormIdAndItemAndLearnerId(
                Long.valueOf(lmsContentPackageID), activityID, Long.valueOf(learnerID));
        ScormData scormData;
        if (scormDataList == null || scormDataList.size() != 1) {
            scormData = new ScormData();
            scormData.setCreateTime(new Date());
            com.corkili.learningserver.po.Scorm scorm = new com.corkili.learningserver.po.Scorm();
            scorm.setId(Long.valueOf(lmsContentPackageID));
            scormData.setScorm(scorm);
            scormData.setItem(activityID);
            com.corkili.learningserver.po.User user = new com.corkili.learningserver.po.User();
            user.setId(Long.valueOf(learnerID));
            scormData.setLearner(user);
        } else {
            scormData = scormDataList.get(0);
        }
        scormData.setUpdateTime(new Date());
        scormData.setRuntimeData(runtimeData);
        scormDataRepository.save(scormData);
    }
}
