package com.corkili.learningserver.service.impl;

import com.corkili.learningserver.bo.CourseCatalog;
import com.corkili.learningserver.common.ScormZipUtils;
import com.corkili.learningserver.scorm.SCORM;
import com.corkili.learningserver.scorm.cam.load.SCORMPackageManager;
import com.corkili.learningserver.scorm.cam.model.ContentPackage;
import com.corkili.learningserver.scorm.rte.api.SCORMRuntimeManager;
import com.corkili.learningserver.scorm.sn.api.SCORMSeqNavManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.Scorm;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.repo.ScormRepository;
import com.corkili.learningserver.service.ScormService;

import java.util.Optional;

@Slf4j
@Service
@Transactional
public class ScormServiceImpl extends ServiceImpl<Scorm, com.corkili.learningserver.po.Scorm> implements ScormService {

    @Autowired
    private ScormRepository scormRepository;

    private SCORM scormManager;

    private SCORMPackageManager scormPackageManager;

    private SCORMRuntimeManager scormRuntimeManager;

    private SCORMSeqNavManager scormSeqNavManager;

    public ScormServiceImpl() {
        scormManager = SCORM.getInstance();
        scormPackageManager = scormManager.getPackageManager();
        scormRuntimeManager = scormManager.getRuntimeManager();
        scormSeqNavManager = scormManager.getSnManager();
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
}
