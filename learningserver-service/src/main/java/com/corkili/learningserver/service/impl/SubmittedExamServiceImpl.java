package com.corkili.learningserver.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.SubmittedExam;
import com.corkili.learningserver.repo.SubmittedExamRepository;
import com.corkili.learningserver.service.SubmittedExamService;

@Slf4j
@Service
public class SubmittedExamServiceImpl extends ServiceImpl<SubmittedExam, com.corkili.learningserver.po.SubmittedExam> implements SubmittedExamService {

    @Autowired
    private SubmittedExamRepository submittedExamRepository;

    @Override
    public Optional<SubmittedExam> po2bo(com.corkili.learningserver.po.SubmittedExam submittedExamPO) {
        Optional<SubmittedExam> superOptional = super.po2bo(submittedExamPO);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        SubmittedExam submittedExam = superOptional.get();
        // TODO submittedAnswers
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
}
