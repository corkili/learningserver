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

import com.corkili.learningserver.bo.CourseWork;
import com.corkili.learningserver.bo.WorkQuestion;
import com.corkili.learningserver.common.ControllerUtils;
import com.corkili.learningserver.common.ProtoUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.generate.protobuf.Info.CourseWorkInfo;
import com.corkili.learningserver.generate.protobuf.Request.CourseWorkCreateRequest;
import com.corkili.learningserver.generate.protobuf.Response.BaseResponse;
import com.corkili.learningserver.generate.protobuf.Response.CourseWorkCreateResponse;
import com.corkili.learningserver.service.CourseWorkService;
import com.corkili.learningserver.token.TokenManager;

@Controller
@RequestMapping("/courseWork")
public class CourseWorkController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private CourseWorkService courseWorkService;

    @ResponseBody
    @RequestMapping(value = "/create", produces = "application/x-protobuf", method = RequestMethod.POST)
    public CourseWorkCreateResponse createCourseWork(@RequestBody CourseWorkCreateRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return CourseWorkCreateResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        CourseWork courseWork = new CourseWork();
        courseWork.setOpen(request.getOpen());
        courseWork.setWorkName(request.getCourseWorkName());
        courseWork.setBelongCourseId(request.getBelongCourseId());
        courseWork.setDeadline(request.getHasDeadline() ? new Date(request.getDeadline()) : null);
        List<WorkQuestion> workQuestions = new ArrayList<>(request.getQuestionIdCount());
        for (Entry<Integer, Long> entry : request.getQuestionIdMap().entrySet()) {
            WorkQuestion workQuestion = new WorkQuestion();
            workQuestion.setIndex(entry.getKey());
            workQuestion.setQuestionId(entry.getValue());
            workQuestions.add(workQuestion);
        }
        ServiceResult serviceResult = courseWorkService.createCourseWork(courseWork, workQuestions);
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        CourseWorkInfo courseWorkInfo;
        if (serviceResult.isSuccess()) {
            courseWorkInfo = ProtoUtils.generateCourseWorkInfo((CourseWork) serviceResult.extra(CourseWork.class),
                    (List<WorkQuestion>) serviceResult.extra(List.class));
        } else {
            courseWorkInfo = ProtoUtils.generateCourseWorkInfo(courseWork, workQuestions);
        }
        return CourseWorkCreateResponse.newBuilder()
                .setResponse(baseResponse)
                .setCourseWorkInfo(courseWorkInfo)
                .build();
    }

}
