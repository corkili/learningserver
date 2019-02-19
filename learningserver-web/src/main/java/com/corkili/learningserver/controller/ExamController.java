package com.corkili.learningserver.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

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
import com.corkili.learningserver.generate.protobuf.Info.ExamInfo;
import com.corkili.learningserver.generate.protobuf.Info.Score;
import com.corkili.learningserver.generate.protobuf.Request.ExamCreateRequest;
import com.corkili.learningserver.generate.protobuf.Response.BaseResponse;
import com.corkili.learningserver.generate.protobuf.Response.ExamCreateResponse;
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

}
