package com.corkili.learningserver.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.generate.protobuf.Info;
import com.corkili.learningserver.generate.protobuf.Info.QuestionInfo;
import com.corkili.learningserver.generate.protobuf.Info.QuestionSimpleInfo;
import com.corkili.learningserver.generate.protobuf.Request.QuestionFindAllRequest;
import com.corkili.learningserver.generate.protobuf.Request.QuestionGetRequest;
import com.corkili.learningserver.generate.protobuf.Request.QuestionImportRequest;
import com.corkili.learningserver.generate.protobuf.Request.QuestionUpdateRequest;
import com.corkili.learningserver.generate.protobuf.Response.BaseResponse;
import com.corkili.learningserver.generate.protobuf.Response.QuestionFindAllResponse;
import com.corkili.learningserver.generate.protobuf.Response.QuestionGetResponse;
import com.corkili.learningserver.generate.protobuf.Response.QuestionImportResponse;
import com.corkili.learningserver.generate.protobuf.Response.QuestionUpdateResponse;
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

    @ResponseBody
    @RequestMapping(value = "/findAll", produces = "application/x-protobuf", method = RequestMethod.POST)
    public QuestionFindAllResponse findAllQuestion(@RequestBody QuestionFindAllRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return QuestionFindAllResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Long authorId = tokenManager.getUserIdAssociatedWithToken(token);
        List<String> keywordList = !request.getByKeyword() ? null : new LinkedList<>(request.getKeywordList());
        List<QuestionType> questionTypeList = null;
        if (request.getByQuestionType()) {
            questionTypeList = new LinkedList<>();
            for (Info.QuestionType questionType : request.getQuestionTypeList()) {
                questionTypeList.add(QuestionType.valueOf(questionType.name()));
            }
        }
        ServiceResult serviceResult = questionService.findAllQuestion(authorId, request.getAll(), keywordList, questionTypeList);
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        List<QuestionSimpleInfo> questionSimpleInfoList = new LinkedList<>();
        if (serviceResult.isSuccess()) {
            List<Question> questionList = (List<Question>) serviceResult.extra(List.class);
            for (Question question : questionList) {
                questionSimpleInfoList.add(ProtoUtils.generateQuestionSimpleInfo(question));
            }
        }
        return QuestionFindAllResponse.newBuilder()
                .setResponse(baseResponse)
                .addAllQuestionSimpleInfo(questionSimpleInfoList)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/get", produces = "application/x-protobuf", method = RequestMethod.POST)
    public QuestionGetResponse getQuestion(@RequestBody QuestionGetRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return QuestionGetResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        if (request.getQuestionIdCount() <= 0) {
            return QuestionGetResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "no question id exists"))
                    .build();
        }
        List<QuestionInfo> questionInfoList = new LinkedList<>();
        StringBuilder errId = new StringBuilder();
        List<Long> errIdList = new LinkedList<>();
        int i = 0;
        for (Long questionId : request.getQuestionIdList()) {
            Optional<Question> questionOptional = questionService.retrieve(questionId);
            i++;
            if (questionOptional.isPresent()) {
                questionInfoList.add(ProtoUtils.generateQuestionInfo(questionOptional.get(), request.getLoadImage()));
            } else {
                errIdList.add(questionId);
                errId.append(questionId);
                if (i != request.getQuestionIdCount()) {
                    errId.append(",");
                }
            }
        }
        String msg = "get all question success";
        if (errId.length() != 0) {
            msg = ServiceUtils.format("get some question success, but get question [{}] failed.", errId.toString());
        }
        return QuestionGetResponse.newBuilder()
                .setResponse(ControllerUtils.generateSuccessBaseResponse(token, msg))
                .addAllQuestionInfo(questionInfoList)
                .addAllFailedQuestionId(errIdList)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/update", produces = "application/x-protobuf", method = RequestMethod.POST)
    public QuestionUpdateResponse updateQuestion(@RequestBody QuestionUpdateRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return QuestionUpdateResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Long authorId = tokenManager.getUserIdAssociatedWithToken(token);
        Optional<Question> questionOptional = questionService.retrieve(request.getQuestionId());
        if (!questionOptional.isPresent()) {
            return QuestionUpdateResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, ServiceUtils.format(
                            "question [{}] not exist", request.getQuestionId())))
                    .build();
        }
        Question copyQuestion = Question.copyFrom(questionOptional.get());
        if (request.getUpdateQuestion()) {
            copyQuestion.setQuestion(request.getQuestion());
        }
        Map<String, byte[]> images = null;
        if (request.getUpdateImage()) {
            images = ControllerUtils.generateImageMap("question", authorId, request.getImageList());
        }
        if (request.getUpdateQuestionType()) {
            copyQuestion.setQuestionType(QuestionType.valueOf(request.getQuestionType().name()));
        }
        if (request.getUpdateAutoCheck()) {
            copyQuestion.setAutoCheck(request.getAutoCheck());
        }
        if (request.getUpdateChoices()) {
            request.getChoicesMap().forEach(copyQuestion::putChoice);
        }
        Map<String, byte[]> essayImages = null;
        if (request.getUpdateAnswer()) {
            copyQuestion.setAnswer(ProtoUtils.generateQuestionAnswer(request.getQuestionType(), request.getAnswer()));
            if (request.getQuestionType() == Info.QuestionType.Essay && request.getAnswer().hasEssayAnswer()) {
                essayImages = ControllerUtils.generateImageMap("question", authorId,
                        request.getAnswer().getEssayAnswer().getImageList());
            }
        }
        ServiceResult serviceResult = questionService.updateQuestion(copyQuestion, images, essayImages);
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        QuestionInfo questionInfo;
        if (serviceResult.isSuccess()) {
            questionInfo = ProtoUtils.generateQuestionInfo((Question) serviceResult.extra(Question.class), false);
        } else {
            questionInfo = ProtoUtils.generateQuestionInfo(copyQuestion, false);
        }
        return QuestionUpdateResponse.newBuilder()
                .setResponse(baseResponse)
                .setQuestionInfo(questionInfo)
                .build();
    }
}
