package com.corkili.learningserver.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.corkili.learningserver.bo.Question;
import com.corkili.learningserver.bo.QuestionType;
import com.corkili.learningserver.common.ControllerUtils;
import com.corkili.learningserver.common.ProtoUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.generate.protobuf.Info;
import com.corkili.learningserver.generate.protobuf.Info.QuestionInfo;
import com.corkili.learningserver.generate.protobuf.Request.QuestionImportRequest;
import com.corkili.learningserver.generate.protobuf.Response.BaseResponse;
import com.corkili.learningserver.generate.protobuf.Response.QuestionImportResponse;
import com.corkili.learningserver.service.QuestionService;
import com.corkili.learningserver.token.TokenManager;

@Controller
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private QuestionService questionService;

    @ResponseBody
    @RequestMapping(value = "/import", produces = "application/x-protobuf", method = RequestMethod.POST)
    public QuestionImportResponse importQuestion(@RequestBody QuestionImportRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return QuestionImportResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Long authorId = tokenManager.getUserIdAssociatedWithToken(token);
        Question question = new Question();
        question.setQuestion(request.getQuestion());
        Map<String, byte[]> images = ControllerUtils.generateImageMap("question", authorId, request.getImageList());
        question.setQuestionType(QuestionType.valueOf(request.getQuestionType().name()));
        question.setAutoCheck(request.getAutoCheck());
        request.getChoicesMap().forEach(question::putChoice);
        question.setAnswer(ProtoUtils.generateQuestionAnswer(request.getQuestionType(), request.getAnswer()));
        Map<String, byte[]> essayImages = null;
        if (request.getQuestionType() == Info.QuestionType.Essay && request.getAnswer().hasEssayAnswer()) {
            essayImages = ControllerUtils.generateImageMap("question", authorId,
                    request.getAnswer().getEssayAnswer().getImageList());
        }
        question.setAuthorId(authorId);
        ServiceResult serviceResult = questionService.importQuestion(question, images, essayImages);
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        QuestionInfo questionInfo;
        if (serviceResult.isSuccess()) {
            questionInfo = ProtoUtils.generateQuestionInfo((Question) serviceResult.extra(Question.class), false);
        } else {
            questionInfo = ProtoUtils.generateQuestionInfo(question, false);
        }
        return QuestionImportResponse.newBuilder()
                .setResponse(baseResponse)
                .setQuestionInfo(questionInfo)
                .build();
    }

}
