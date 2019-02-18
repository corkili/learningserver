package com.corkili.learningserver.service.impl;

import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.Question;
import com.corkili.learningserver.bo.Question.EssayAnswer;
import com.corkili.learningserver.bo.Question.MultipleChoiceAnswer;
import com.corkili.learningserver.bo.Question.MultipleFillingAnswer;
import com.corkili.learningserver.bo.Question.SingleChoiceAnswer;
import com.corkili.learningserver.bo.Question.SingleFillingAnswer;
import com.corkili.learningserver.bo.QuestionType;
import com.corkili.learningserver.bo.User;
import com.corkili.learningserver.common.ImageUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.po.Question.Type;
import com.corkili.learningserver.repo.QuestionRepository;
import com.corkili.learningserver.service.QuestionService;
import com.corkili.learningserver.service.UserService;

@Slf4j
@Service
public class QuestionServiceImpl extends ServiceImpl<Question, com.corkili.learningserver.po.Question> implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserService userService;

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

    @Override
    protected Logger logger() {
        return log;
    }

    @Override
    public ServiceResult importQuestion(Question question, Map<String, byte[]> questionImages, Map<String, byte[]> essayImages) {
        if (StringUtils.isBlank(question.getQuestion())) {
            return recordErrorAndCreateFailResultWithMessage("import question error: question is empty");
        }
        if (question.getQuestionType() == null) {
            return recordErrorAndCreateFailResultWithMessage("import question error: question type is null");
        }
        switch (question.getQuestionType()) {
            case SingleChoice:
            case MultipleChoice:
                question.setAutoCheck(true);
                if (question.getChoices() == null || question.getChoices().isEmpty()) {
                    return recordErrorAndCreateFailResultWithMessage("import question error: choices of choice question is empty");
                }
                break;
            case Essay:
                question.setAutoCheck(false);
                break;
        }
        boolean notMatches = false;
        switch (question.getQuestionType()) {
            case SingleFilling:
                if (!(question.getAnswer() instanceof SingleFillingAnswer)) {
                    notMatches = true;
                }
                break;
            case MultipleFilling:
                if (!(question.getAnswer() instanceof MultipleFillingAnswer)) {
                    notMatches = true;
                }
                break;
            case SingleChoice:
                if (!(question.getAnswer() instanceof SingleChoiceAnswer)) {
                    notMatches = true;
                }
                break;
            case MultipleChoice:
                if (!(question.getAnswer() instanceof MultipleChoiceAnswer)) {
                    notMatches = true;
                }
                break;
            case Essay:
                if (!(question.getAnswer() instanceof EssayAnswer)) {
                    notMatches = true;
                }
                break;
        }
        if (notMatches) {
            return recordErrorAndCreateFailResultWithMessage("import question error: answer type and question type is not matched");
        }
        Optional<User> userOptional = userService.retrieve(question.getAuthorId());
        if (!userOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage("import question error: user info of author [{}] not exist",
                    question.getAuthorId() == null ? "" : question.getAuthorId());
        }
        // questionImages
        if (!ImageUtils.storeImages(questionImages)) {
            return recordErrorAndCreateFailResultWithMessage("import question error: store images of question failed");
        }
        question.getImagePaths().clear();
        question.getImagePaths().addAll(questionImages.keySet());
        // essayImages
        if (question.getQuestionType() == QuestionType.Essay) {
            EssayAnswer essayAnswer = (EssayAnswer) question.getAnswer();
            if (!ImageUtils.storeImages(essayImages)) {
                ImageUtils.deleteImages(questionImages.keySet());
                return recordErrorAndCreateFailResultWithMessage("import question error: store images of essay answer failed");
            }
            essayAnswer.getImagePaths().clear();
            if (essayImages != null) {
                essayAnswer.getImagePaths().addAll(questionImages.keySet());
            }
        }
        Optional<Question> questionOptional = create(question);
        if (!questionOptional.isPresent()) {
            ImageUtils.deleteImages(questionImages.keySet());
            if (essayImages != null) {
                ImageUtils.deleteImages(essayImages.keySet());
            }
            return recordErrorAndCreateFailResultWithMessage("import question error: store into db failed");
        }
        question = questionOptional.get();
        return ServiceResult.successResult("import question success", Question.class, question);
    }


}
