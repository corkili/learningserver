package com.corkili.learningserver.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.QuestionType;
import com.corkili.learningserver.bo.SubmittedExam;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.po.ExamQuestion;
import com.corkili.learningserver.repo.ExamQuestionRepository;
import com.corkili.learningserver.repo.SubmittedExamRepository;
import com.corkili.learningserver.service.SubmittedExamService;

@Slf4j
@Service
public class SubmittedExamServiceImpl extends ServiceImpl<SubmittedExam, com.corkili.learningserver.po.SubmittedExam> implements SubmittedExamService {

    @Autowired
    private SubmittedExamRepository submittedExamRepository;

    @Autowired
    private ExamQuestionRepository examQuestionRepository;

    @Override
    public Optional<SubmittedExam> po2bo(com.corkili.learningserver.po.SubmittedExam submittedExamPO) {
        Optional<SubmittedExam> superOptional = super.po2bo(submittedExamPO);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        SubmittedExam submittedExam = superOptional.get();
        Map<Integer, QuestionType> questionTypeMap = new HashMap<>();
        List<ExamQuestion> examQuestionList = examQuestionRepository.findExamQuestionsByBelongExam(submittedExamPO.getBelongExam());
        for (ExamQuestion examQuestion : examQuestionList) {
            questionTypeMap.put(examQuestion.getIndex(), QuestionType.valueOf(examQuestion.getQuestion().getQuestionType().name()));
        }
        submittedExam.setSubmittedAnswers(submittedExamPO.getSubmittedAnswers(), questionTypeMap);
        return Optional.of(submittedExam);
    }

    @Override
    public Optional<com.corkili.learningserver.po.SubmittedExam> bo2po(SubmittedExam submittedExam) {
        Optional<com.corkili.learningserver.po.SubmittedExam> superOptional = super.bo2po(submittedExam);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        com.corkili.learningserver.po.SubmittedExam submittedExamPO = superOptional.get();
        submittedExamPO.setSubmittedAnswers(submittedExam.getSubmittedAnswersStr());
        return Optional.of(submittedExamPO);
    }

    @Override
    protected JpaRepository<com.corkili.learningserver.po.SubmittedExam, Long> repository() {
        return submittedExamRepository;
    }

    @Override
    protected String entityName() {
        return "submittedExam";
    }

    @Override
    protected SubmittedExam newBusinessObject() {
        return new SubmittedExam();
    }

    @Override
    protected com.corkili.learningserver.po.SubmittedExam newPersistObject() {
        return new com.corkili.learningserver.po.SubmittedExam();
    }

    @Override
    protected Logger logger() {
        return log;
    }

    @Override
    public ServiceResult deleteSubmittedExam(Long submittedExamId) {
        if (!delete(submittedExamId)) {
            return recordWarnAndCreateSuccessResultWithMessage("delete submitted exam success");
        }
        return ServiceResult.successResultWithMesage("delete submitted exam success");
    }

    @Override
    public ServiceResult deleteSubmittedExamByBelongExamId(Long belongExamId) {
        List<Long> submittedExamIdList = submittedExamRepository.findAllSubmittedExamIdByBelongExamId(belongExamId);
        submittedExamRepository.deleteAllByBelongExamId(belongExamId);
        for (Long id : submittedExamIdList) {
            evictFromCache(entityName() + id);
        }
        return ServiceResult.successResult("delete submitted exam success");
    }
}
