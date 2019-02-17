package com.corkili.learningserver.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.WorkQuestion;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.repo.WorkQuestionRepository;
import com.corkili.learningserver.service.WorkQuestionService;

@Slf4j
@Service
public class WorkQuestionServiceImpl extends ServiceImpl<WorkQuestion, com.corkili.learningserver.po.WorkQuestion> implements WorkQuestionService {

    @Autowired
    private WorkQuestionRepository workQuestionRepository;

    @Override
    protected JpaRepository<com.corkili.learningserver.po.WorkQuestion, Long> repository() {
        return workQuestionRepository;
    }

    @Override
    protected String entityName() {
        return "workQuestion";
    }

    @Override
    protected WorkQuestion newBusinessObject() {
        return new WorkQuestion();
    }

    @Override
    protected com.corkili.learningserver.po.WorkQuestion newPersistObject() {
        return new com.corkili.learningserver.po.WorkQuestion();
    }

    @Override
    protected Logger logger() {
        return log;
    }

    @Override
    public ServiceResult deleteWorkQuestion(Long workQuestionId) {
        if (!delete(workQuestionId)) {
            return recordWarnAndCreateSuccessResultWithMessage("delete work question success");
        }
        return ServiceResult.successResultWithMesage("delete work question success");
    }

    @Override
    public ServiceResult deleteWorkQuestionByBelongCourseWorkId(Long belongCourseWorkId) {
        List<Long> workQuestionIdList = workQuestionRepository.findAllWorkQuestionIdByBelongCourseWorkId(belongCourseWorkId);
        workQuestionRepository.deleteAllByBelongCourseWorkId(belongCourseWorkId);
        for (Long id : workQuestionIdList) {
            evictFromCache(entityName() + id);
        }
        return ServiceResult.successResultWithMesage("delete work question success");
    }
}
