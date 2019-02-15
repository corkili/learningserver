package com.corkili.learningserver.service.impl;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.Exam;
import com.corkili.learningserver.repo.ExamRepository;
import com.corkili.learningserver.service.ExamService;

@Slf4j
@Service
public class ExamServiceImpl extends ServiceImpl<Exam, com.corkili.learningserver.po.Exam> implements ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Override
    protected JpaRepository<com.corkili.learningserver.po.Exam, Long> repository() {
        return examRepository;
    }

    @Override
    protected String entityName() {
        return "exam";
    }

    @Override
    protected Exam newBusinessObject() {
        return new Exam();
    }

    @Override
    protected com.corkili.learningserver.po.Exam newPersistObject() {
        return new com.corkili.learningserver.po.Exam();
    }

    @Override
    protected Logger logger() {
        return log;
    }
}
