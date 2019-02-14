package com.corkili.learningserver.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.Question;
import com.corkili.learningserver.bo.QuestionType;
import com.corkili.learningserver.po.Question.Type;
import com.corkili.learningserver.repo.QuestionRepository;
import com.corkili.learningserver.service.QuestionService;

@Slf4j
@Service
public class QuestionServiceImpl extends ServiceImpl<Question, com.corkili.learningserver.po.Question> implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Override
    public Optional<Question> po2bo(com.corkili.learningserver.po.Question questionPO) {
        Optional<Question> superOptional = super.po2bo(questionPO);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        Question question = superOptional.get();
        question.setImagePaths(questionPO.getImagePaths());
        question.setQuestionType(QuestionType.valueOf(questionPO.getQuestionType().name()));
        question.setChoices(questionPO.getChoices());
        question.setAnswer(questionPO.getAnswer(), question.getQuestionType());
        return Optional.of(question);
    }

    @Override
    public Optional<com.corkili.learningserver.po.Question> bo2po(Question question) {
        Optional<com.corkili.learningserver.po.Question> superOptional = super.bo2po(question);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        com.corkili.learningserver.po.Question questionPO = superOptional.get();
        questionPO.setImagePaths(question.getImagePathsStr());
        questionPO.setQuestionType(Type.valueOf(question.getQuestionType().name()));
        questionPO.setChoices(question.getChoicesStr());
        questionPO.setAnswer(question.getAnswer().getAnswer());
        return Optional.of(questionPO);
    }

    @Override
    protected JpaRepository<com.corkili.learningserver.po.Question, Long> repository() {
        return questionRepository;
    }

    @Override
    protected String entityName() {
        return "question";
    }

    @Override
    protected Question newBusinessObject() {
        return new Question();
    }

    @Override
    protected com.corkili.learningserver.po.Question newPersistObject() {
        return new com.corkili.learningserver.po.Question();
    }
}
