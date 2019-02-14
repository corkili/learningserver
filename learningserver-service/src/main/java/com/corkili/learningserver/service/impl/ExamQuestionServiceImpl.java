package com.corkili.learningserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.ExamQuestion;
import com.corkili.learningserver.repo.ExamQuestionRepository;
import com.corkili.learningserver.service.ExamQuestionService;

@Slf4j
@Service
public class ExamQuestionServiceImpl extends ServiceImpl<ExamQuestion, com.corkili.learningserver.po.ExamQuestion> implements ExamQuestionService {

    @Autowired
    private ExamQuestionRepository examQuestionRepository;

    @Override
    protected JpaRepository<com.corkili.learningserver.po.ExamQuestion, Long> repository() {
        return examQuestionRepository;
    }

    @Override
    protected String entityName() {
        return "examQuestion";
    }

    @Override
    protected ExamQuestion newBusinessObject() {
        return new ExamQuestion();
    }

    @Override
    protected com.corkili.learningserver.po.ExamQuestion newPersistObject() {
        return new com.corkili.learningserver.po.ExamQuestion();
    }
}
