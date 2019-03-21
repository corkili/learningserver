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

import com.corkili.learningserver.bo.CourseWork;
import com.corkili.learningserver.bo.WorkQuestion;
import com.corkili.learningserver.common.ControllerUtils;
import com.corkili.learningserver.common.ProtoUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.generate.protobuf.Info.CourseWorkInfo;
import com.corkili.learningserver.generate.protobuf.Info.CourseWorkSimpleInfo;
import com.corkili.learningserver.generate.protobuf.Request.CourseWorkCreateRequest;
import com.corkili.learningserver.generate.protobuf.Request.CourseWorkDeleteRequest;
import com.corkili.learningserver.generate.protobuf.Request.CourseWorkFindAllRequest;
import com.corkili.learningserver.generate.protobuf.Request.CourseWorkGetRequest;
import com.corkili.learningserver.generate.protobuf.Request.CourseWorkUpdateRequest;
import com.corkili.learningserver.generate.protobuf.Response.BaseResponse;
import com.corkili.learningserver.generate.protobuf.Response.CourseWorkCreateResponse;
import com.corkili.learningserver.generate.protobuf.Response.CourseWorkDeleteResponse;
import com.corkili.learningserver.generate.protobuf.Response.CourseWorkFindAllResponse;
import com.corkili.learningserver.generate.protobuf.Response.CourseWorkGetResponse;
import com.corkili.learningserver.generate.protobuf.Response.CourseWorkUpdateResponse;
import com.corkili.learningserver.service.CourseWorkService;
import com.corkili.learningserver.service.WorkQuestionService;
import com.corkili.learningserver.token.TokenManager;

@Controller
@RequestMapping("/courseWork")
public class CourseWorkController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private CourseWorkService courseWorkService;

    @Autowired
    private WorkQuestionService workQuestionService;

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
        courseWork.setOpen(false);
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
            courseWorkInfo = ProtoUtils.generateCourseWorkInfo(serviceResult.extra(CourseWork.class),
                    (List<WorkQuestion>) serviceResult.extra(List.class));
        } else {
            courseWorkInfo = ProtoUtils.generateCourseWorkInfo(courseWork, workQuestions);
        }
        return CourseWorkCreateResponse.newBuilder()
                .setResponse(baseResponse)
                .setCourseWorkInfo(courseWorkInfo)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/update", produces = "application/x-protobuf", method = RequestMethod.POST)
    public CourseWorkUpdateResponse updateCourseWork(@RequestBody CourseWorkUpdateRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return CourseWorkUpdateResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Optional<CourseWork> courseWorkOptional = courseWorkService.retrieve(request.getCourseWorkId());
        if (!courseWorkOptional.isPresent()) {
            return CourseWorkUpdateResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token,
                            ServiceUtils.format("courseWork [{}] not exists", request.getCourseWorkId())))
                    .build();
        }
        CourseWork courseWork = courseWorkOptional.get();
        boolean notOpen = !courseWork.isOpen();
        CourseWork copyCourseWork = CourseWork.copyFrom(courseWork);
        if (request.getUpdateOpen() && notOpen) {
            copyCourseWork.setOpen(request.getOpen());
        }
        if (request.getUpdateCourseWorkName()) {
            copyCourseWork.setWorkName(request.getCourseWorkName());
        }
        if (request.getUpdateDeadline()) {
            copyCourseWork.setDeadline(request.getHasDeadline() ? new Date(request.getDeadline()) : null);
        }
        List<WorkQuestion> workQuestions = null;
        if (request.getUpdateQuestion() && notOpen) {
            workQuestions = new ArrayList<>(request.getQuestionIdCount());
            for (Entry<Integer, Long> entry : request.getQuestionIdMap().entrySet()) {
                WorkQuestion workQuestion = new WorkQuestion();
                workQuestion.setIndex(entry.getKey());
                workQuestion.setQuestionId(entry.getValue());
                workQuestions.add(workQuestion);
            }
        }
        ServiceResult serviceResult = courseWorkService.updateCourseWork(copyCourseWork, workQuestions);
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        CourseWorkInfo courseWorkInfo;
        if (serviceResult.isSuccess()) {
            courseWorkInfo = ProtoUtils.generateCourseWorkInfo(serviceResult.extra(CourseWork.class),
                    (List<WorkQuestion>) serviceResult.extra(List.class));
        } else {
            courseWorkInfo = ProtoUtils.generateCourseWorkInfo(copyCourseWork, workQuestions);
        }
        return CourseWorkUpdateResponse.newBuilder()
                .setResponse(baseResponse)
                .setCourseWorkInfo(courseWorkInfo)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/findAll", produces = "application/x-protobuf", method = RequestMethod.POST)
    public CourseWorkFindAllResponse findAllCourseWork(@RequestBody CourseWorkFindAllRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return CourseWorkFindAllResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        ServiceResult serviceResult = courseWorkService.findAllCourseWork(request.getBelongCourseId());
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        List<CourseWorkSimpleInfo> courseWorkSimpleInfos = new LinkedList<>();
        for (CourseWork courseWork : (List<CourseWork>) serviceResult.extra(List.class)) {
            courseWorkSimpleInfos.add(ProtoUtils.generateCourseWorkSimpleInfo(courseWork));
        }
        return CourseWorkFindAllResponse.newBuilder()
                .setResponse(baseResponse)
                .addAllCourseWorkSimpleInfo(courseWorkSimpleInfos)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/get", produces = "application/x-protobuf", method = RequestMethod.POST)
    public CourseWorkGetResponse getCourseWork(@RequestBody CourseWorkGetRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return CourseWorkGetResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Optional<CourseWork> courseWorkOptional = courseWorkService.retrieve(request.getCourseWorkId());
        if (!courseWorkOptional.isPresent()) {
            return CourseWorkGetResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token,
                            ServiceUtils.format("no courseWork [{}] exists", request.getCourseWorkId())))
                    .build();
        }
        CourseWork courseWork = courseWorkOptional.get();
        ServiceResult serviceResult = workQuestionService
                .findAllWorkQuestionByBelongCourseWorkId(courseWork.getId());
        List<WorkQuestion> workQuestionList = (List<WorkQuestion>) serviceResult.extra(List.class);
        return CourseWorkGetResponse.newBuilder()
                .setResponse(ControllerUtils.generateSuccessBaseResponse(token, "get courseWork success"))
                .setCourseWorkInfo(ProtoUtils.generateCourseWorkInfo(courseWork, workQuestionList))
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/delete", produces = "application/x-protobuf", method = RequestMethod.POST)
    public CourseWorkDeleteResponse getCourseWork(@RequestBody CourseWorkDeleteRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return CourseWorkDeleteResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        ServiceResult serviceResult = courseWorkService.deleteCourseWork(request.getCourseWorkId());
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        return CourseWorkDeleteResponse.newBuilder()
                .setResponse(baseResponse)
                .setCourseWorkId(request.getCourseWorkId())
                .build();
    }

}
