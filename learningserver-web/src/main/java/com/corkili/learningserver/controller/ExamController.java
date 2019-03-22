package com.corkili.learningserver.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.corkili.learningserver.bo.Exam;
import com.corkili.learningserver.bo.ExamQuestion;
import com.corkili.learningserver.common.ControllerUtils;
import com.corkili.learningserver.common.ProtoUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.generate.protobuf.Info.ExamInfo;
import com.corkili.learningserver.generate.protobuf.Info.ExamSimpleInfo;
import com.corkili.learningserver.generate.protobuf.Info.Score;
import com.corkili.learningserver.generate.protobuf.Request.ExamDeleteRequest;
import com.corkili.learningserver.generate.protobuf.Request.ExamCreateRequest;
import com.corkili.learningserver.generate.protobuf.Request.ExamFindAllRequest;
import com.corkili.learningserver.generate.protobuf.Request.ExamGetRequest;
import com.corkili.learningserver.generate.protobuf.Request.ExamUpdateRequest;
import com.corkili.learningserver.generate.protobuf.Response.BaseResponse;
import com.corkili.learningserver.generate.protobuf.Response.ExamDeleteResponse;
import com.corkili.learningserver.generate.protobuf.Response.ExamCreateResponse;
import com.corkili.learningserver.generate.protobuf.Response.ExamFindAllResponse;
import com.corkili.learningserver.generate.protobuf.Response.ExamGetResponse;
import com.corkili.learningserver.generate.protobuf.Response.ExamUpdateResponse;
import com.corkili.learningserver.service.ExamQuestionService;
import com.corkili.learningserver.service.ExamService;
import com.corkili.learningserver.token.TokenManager;

@Controller
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private ExamService examService;

    @Autowired
    private ExamQuestionService examQuestionService;

    @ResponseBody
    @RequestMapping(value = "/create", produces = "application/x-protobuf", method = RequestMethod.POST)
    public ExamCreateResponse createExam(@RequestBody ExamCreateRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return ExamCreateResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Exam exam = new Exam();
        exam.setExamName(request.getExamName());
        exam.setBelongCourseId(request.getBelongCourseId());
        exam.setStartTime(new Date(request.getStartTime()));
        exam.setEndTime(new Date(request.getEndTime()));
        exam.setDuration(10);
        List<ExamQuestion> examQuestions = new ArrayList<>(request.getQuestionIdCount());
        for (Entry<Integer, Long> entry : request.getQuestionIdMap().entrySet()) {
            ExamQuestion examQuestion = new ExamQuestion();
            examQuestion.setIndex(entry.getKey());
            examQuestion.setQuestionId(entry.getValue());
            Score questionScore = request.getQuestionScoreMap().get(entry.getKey());
            if (questionScore == null) {
                examQuestion.setScore(0);
            } else {
                if (questionScore.hasMultipleScore()) {
                    examQuestion.getScoreMap().clear();
                    examQuestion.getScoreMap().putAll(questionScore.getMultipleScore().getScoreMap());
                } else {
                    examQuestion.setScore(questionScore.getSingleScore());
                }
            }
            examQuestions.add(examQuestion);
        }
        ServiceResult serviceResult = examService.createExam(exam, examQuestions);
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        ExamInfo examInfo;
        if (serviceResult.isSuccess()) {
            examInfo = ProtoUtils.generateExamInfo(serviceResult.extra(Exam.class),
                    (List<ExamQuestion>) serviceResult.extra(List.class));
        } else {
            examInfo = ProtoUtils.generateExamInfo(exam, examQuestions);
        }
        return ExamCreateResponse.newBuilder()
                .setResponse(baseResponse)
                .setExamInfo(examInfo)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/update", produces = "application/x-protobuf", method = RequestMethod.POST)
    public ExamUpdateResponse updateExam(@RequestBody ExamUpdateRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return ExamUpdateResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Optional<Exam> examOptional = examService.retrieve(request.getExamId());
        if (!examOptional.isPresent()) {
            return ExamUpdateResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token,
                            ServiceUtils.format("exam [{}] not exists", request.getExamId())))
                    .build();
        }
        Exam exam = examOptional.get();
        boolean notStart = exam.getStartTime().after(new Date());
        Exam copyExam = Exam.copyFrom(exam);
        if (request.getUpdateExamName()) {
            copyExam.setExamName(request.getExamName());
        }
        if (request.getUpdateStartTime() && notStart) {
            copyExam.setStartTime(new Date(request.getStartTime()));
        }
        if (request.getUpdateEndTime()) {
            copyExam.setEndTime(new Date(request.getEndTime()));
        }
        if (request.getUpdateDuration() && notStart) {
            copyExam.setDuration(10);
        }
        List<ExamQuestion> examQuestions = null;
        if (request.getUpdateQuestion() && notStart) {
            examQuestions = new ArrayList<>(request.getQuestionIdCount());
            for (Entry<Integer, Long> entry : request.getQuestionIdMap().entrySet()) {
                ExamQuestion examQuestion = new ExamQuestion();
                examQuestion.setIndex(entry.getKey());
                examQuestion.setQuestionId(entry.getValue());
                Score questionScore = request.getQuestionScoreMap().get(entry.getKey());
                if (questionScore == null) {
                    examQuestion.setScore(0);
                } else {
                    if (questionScore.hasMultipleScore()) {
                        examQuestion.getScoreMap().clear();
                        examQuestion.getScoreMap().putAll(questionScore.getMultipleScore().getScoreMap());
                    } else {
                        examQuestion.setScore(questionScore.getSingleScore());
                    }
                }
                examQuestions.add(examQuestion);
            }
        }
        ServiceResult serviceResult = examService.updateExam(copyExam, request.getUpdateStartTime(), request.getUpdateEndTime(), examQuestions);
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        ExamInfo examInfo;
        if (serviceResult.isSuccess()) {
            examInfo = ProtoUtils.generateExamInfo(serviceResult.extra(Exam.class),
                    (List<ExamQuestion>) serviceResult.extra(List.class));
        } else {
            examInfo = ProtoUtils.generateExamInfo(copyExam, examQuestions);
        }
        return ExamUpdateResponse.newBuilder()
                .setResponse(baseResponse)
                .setExamInfo(examInfo)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/findAll", produces = "application/x-protobuf", method = RequestMethod.POST)
    public ExamFindAllResponse findAllExam(@RequestBody ExamFindAllRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return ExamFindAllResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        ServiceResult serviceResult = examService.findAllExam(request.getBelongCourseId());
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        List<ExamSimpleInfo> examSimpleInfos = new LinkedList<>();
        for (Exam exam : (List<Exam>) serviceResult.extra(List.class)) {
            examSimpleInfos.add(ProtoUtils.generateExamSimpleInfo(exam));
        }
        return ExamFindAllResponse.newBuilder()
                .setResponse(baseResponse)
                .addAllExamSimpleInfo(examSimpleInfos)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/get", produces = "application/x-protobuf", method = RequestMethod.POST)
    public ExamGetResponse getExam(@RequestBody ExamGetRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return ExamGetResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Optional<Exam> examOptional = examService.retrieve(request.getExamId());
        if (!examOptional.isPresent()) {
            return ExamGetResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token,
                            ServiceUtils.format("no exam [{}] exists", request.getExamId())))
                    .build();
        }
        Exam exam = examOptional.get();
        ServiceResult serviceResult = examQuestionService
                .findAllExamQuestionByBelongExamId(exam.getId());
        List<ExamQuestion> examQuestionList = (List<ExamQuestion>) serviceResult.extra(List.class);
        return ExamGetResponse.newBuilder()
                .setResponse(ControllerUtils.generateSuccessBaseResponse(token, "get exam success"))
                .setExamInfo(ProtoUtils.generateExamInfo(exam, examQuestionList))
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/delete", produces = "application/x-protobuf", method = RequestMethod.POST)
    public ExamDeleteResponse getExam(@RequestBody ExamDeleteRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return ExamDeleteResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        ServiceResult serviceResult = examService.deleteExam(request.getExamId());
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        return ExamDeleteResponse.newBuilder()
                .setResponse(baseResponse)
                .setExamId(request.getExamId())
                .build();
    }

}
