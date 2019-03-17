package com.corkili.learningserver.service.impl;

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

@Slf4j
@Service
@Transactional
public class ScormServiceImpl extends ServiceImpl<Scorm, com.corkili.learningserver.po.Scorm> implements ScormService {

    @Autowired
    private ScormRepository scormRepository;

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
    public ServiceResult deleteScorm(Long scormId) {
        if (!delete(scormId)) {
            return recordWarnAndCreateSuccessResultWithMessage("delete scorm success");
        }
        return ServiceResult.successResultWithMesage("delete scorm success");
    }
}
