package com.corkili.learningserver.controller;

import java.util.ArrayList;
import java.util.Date;
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
import com.corkili.learningserver.generate.protobuf.Info.Score;
import com.corkili.learningserver.generate.protobuf.Request.ExamCreateRequest;
import com.corkili.learningserver.generate.protobuf.Request.ExamUpdateRequest;
import com.corkili.learningserver.generate.protobuf.Response.BaseResponse;
import com.corkili.learningserver.generate.protobuf.Response.ExamCreateResponse;
import com.corkili.learningserver.generate.protobuf.Response.ExamUpdateResponse;
import com.corkili.learningserver.service.ExamService;
import com.corkili.learningserver.token.TokenManager;

@Controller
@RequestMapping("/exam")
public class ExamController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private ExamService examService;

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
        exam.setDuration(request.getDuration());
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
            copyExam.setDuration(request.getDuration());
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
        ServiceResult serviceResult = examService.updateExam(copyExam, examQuestions);
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

}
