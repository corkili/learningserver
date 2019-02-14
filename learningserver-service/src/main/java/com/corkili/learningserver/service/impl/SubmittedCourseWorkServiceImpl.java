package com.corkili.learningserver.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.QuestionType;
import com.corkili.learningserver.bo.SubmittedCourseWork;
import com.corkili.learningserver.po.WorkQuestion;
import com.corkili.learningserver.repo.SubmittedCourseWorkRepository;
import com.corkili.learningserver.repo.WorkQuestionRepository;
import com.corkili.learningserver.service.SubmittedCourseWorkService;

@Slf4j
@Service
public class SubmittedCourseWorkServiceImpl extends ServiceImpl<SubmittedCourseWork, com.corkili.learningserver.po.SubmittedCourseWork> implements SubmittedCourseWorkService {

    @Autowired
    private SubmittedCourseWorkRepository submittedCourseWorkRepository;

    @Autowired
    private WorkQuestionRepository workQuestionRepository;
    
    @Override
    public Optional<SubmittedCourseWork> po2bo(com.corkili.learningserver.po.SubmittedCourseWork submittedCourseWorkPO) {
        Optional<SubmittedCourseWork> superOptional = super.po2bo(submittedCourseWorkPO);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        SubmittedCourseWork submittedCourseWork = superOptional.get();
        Map<Integer, QuestionType> questionTypeMap = new HashMap<>();
        List<WorkQuestion> workQuestionList = workQuestionRepository.findWorkQuestionsByBelongCourseWork(submittedCourseWorkPO.getBelongCourseWork());
        for (WorkQuestion workQuestion : workQuestionList) {
            questionTypeMap.put(workQuestion.getIndex(), QuestionType.valueOf(workQuestion.getQuestion().getQuestionType().name()));
        }
        submittedCourseWork.setSubmittedAnswers(submittedCourseWorkPO.getSubmittedAnswers(), questionTypeMap);
        return Optional.of(submittedCourseWork);
    }

    @Override
    public Optional<com.corkili.learningserver.po.SubmittedCourseWork> bo2po(SubmittedCourseWork submittedCourseWork) {
        Optional<com.corkili.learningserver.po.SubmittedCourseWork> superOptional = super.bo2po(submittedCourseWork);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        com.corkili.learningserver.po.SubmittedCourseWork submittedCourseWorkPO = superOptional.get();
        submittedCourseWorkPO.setSubmittedAnswers(submittedCourseWork.getSubmittedAnswersStr());
        return Optional.of(submittedCourseWorkPO);
    }

    @Override
    protected JpaRepository<com.corkili.learningserver.po.SubmittedCourseWork, Long> repository() {
        return submittedCourseWorkRepository;
    }

    @Override
    protected String entityName() {
        return "submittedCourseWork";
    }

    @Override
    protected SubmittedCourseWork newBusinessObject() {
        return new SubmittedCourseWork();
    }

    @Override
    protected com.corkili.learningserver.po.SubmittedCourseWork newPersistObject() {
        return new com.corkili.learningserver.po.SubmittedCourseWork();
    }
}
