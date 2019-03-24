package com.corkili.learningserver.controller;

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

import com.corkili.learningserver.bo.SubmittedCourseWork;
import com.corkili.learningserver.bo.User;
import com.corkili.learningserver.common.ControllerUtils;
import com.corkili.learningserver.common.ProtoUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.generate.protobuf.Info.CourseWorkSubmittedAnswer;
import com.corkili.learningserver.generate.protobuf.Info.SubmittedAnswer;
import com.corkili.learningserver.generate.protobuf.Info.SubmittedCourseWorkInfo;
import com.corkili.learningserver.generate.protobuf.Info.SubmittedCourseWorkSimpleInfo;
import com.corkili.learningserver.generate.protobuf.Request.SubmittedCourseWorkCreateRequest;
import com.corkili.learningserver.generate.protobuf.Request.SubmittedCourseWorkDeleteRequest;
import com.corkili.learningserver.generate.protobuf.Request.SubmittedCourseWorkFindAllRequest;
import com.corkili.learningserver.generate.protobuf.Request.SubmittedCourseWorkGetRequest;
import com.corkili.learningserver.generate.protobuf.Request.SubmittedCourseWorkUpdateRequest;
import com.corkili.learningserver.generate.protobuf.Response.BaseResponse;
import com.corkili.learningserver.generate.protobuf.Response.SubmittedCourseWorkCreateResponse;
import com.corkili.learningserver.generate.protobuf.Response.SubmittedCourseWorkDeleteResponse;
import com.corkili.learningserver.generate.protobuf.Response.SubmittedCourseWorkFindAllResponse;
import com.corkili.learningserver.generate.protobuf.Response.SubmittedCourseWorkGetResponse;
import com.corkili.learningserver.generate.protobuf.Response.SubmittedCourseWorkUpdateResponse;
import com.corkili.learningserver.service.SubmittedCourseWorkService;
import com.corkili.learningserver.service.UserService;
import com.corkili.learningserver.token.TokenManager;

@Controller
@RequestMapping("/submittedCourseWork")
public class SubmittedCourseWorkController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private SubmittedCourseWorkService submittedCourseWorkService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/create", produces = "application/x-protobuf", method = RequestMethod.POST)
    public SubmittedCourseWorkCreateResponse createSubmittedCourseWork(@RequestBody SubmittedCourseWorkCreateRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return SubmittedCourseWorkCreateResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Long submitterId = tokenManager.getUserIdAssociatedWithToken(token);
        SubmittedCourseWork submittedCourseWork = new SubmittedCourseWork();
        for (Entry<Integer, SubmittedAnswer> entry : request.getSubmittedAnswerMap().entrySet()) {
            submittedCourseWork.putNewSubmittedAnswer(entry.getKey(), ProtoUtils.generateSubmittedAnswer(entry.getValue()));
        }
        submittedCourseWork.setFinished(request.getFinished());
        submittedCourseWork.setBelongCourseWorkId(request.getBelongCourseWorkId());
        submittedCourseWork.setSubmitterId(submitterId);
        ServiceResult serviceResult = submittedCourseWorkService.createSubmittedCourseWork(submittedCourseWork);
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        SubmittedCourseWorkInfo submittedCourseWorkInfo;
        if (serviceResult.isSuccess()) {
            submittedCourseWorkInfo = ProtoUtils.generateSubmittedCourseWorkInfo(
                    serviceResult.extra(SubmittedCourseWork.class), serviceResult.extra(User.class));
        } else {
            submittedCourseWorkInfo = ProtoUtils.generateSubmittedCourseWorkInfo(submittedCourseWork, null);
        }
        return SubmittedCourseWorkCreateResponse.newBuilder()
                .setResponse(baseResponse)
                .setSubmittedCourseWorkInfo(submittedCourseWorkInfo)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/update", produces = "application/x-protobuf", method = RequestMethod.POST)
    public SubmittedCourseWorkUpdateResponse updateSubmittedCourseWork(@RequestBody SubmittedCourseWorkUpdateRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return SubmittedCourseWorkUpdateResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Optional<SubmittedCourseWork> submittedCourseWorkOptional = submittedCourseWorkService
                .retrieve(request.getSubmittedCourseWorkId());
        if (!submittedCourseWorkOptional.isPresent()) {
            return SubmittedCourseWorkUpdateResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, ServiceUtils.format(
                            "submittedCourseWork [{}] not exists", request.getSubmittedCourseWorkId())))
                    .build();
        }
        SubmittedCourseWork submittedCourseWork = SubmittedCourseWork.copyFrom(submittedCourseWorkOptional.get());
        boolean oldFinished = submittedCourseWork.isFinished();
        if (request.getUpdateSubmittedAnswer()) {
            for (Entry<Integer, CourseWorkSubmittedAnswer> entry : request.getSubmittedAnswerMap().entrySet()) {
                SubmittedCourseWork.InnerSubmittedAnswer isa = submittedCourseWork.getSubmittedAnswers().get(entry.getKey());
                isa.setSubmittedAnswer(ProtoUtils.generateSubmittedAnswer(entry.getValue().getSubmittedAnswer()));
                isa.setCheckStatus(entry.getValue().getCheckStatus());
            }
        }
        if (request.getUpdateFinished()) {
            submittedCourseWork.setFinished(request.getFinished());
        }
        ServiceResult serviceResult = submittedCourseWorkService.updateSubmittedCourseWork(
                submittedCourseWork, oldFinished, tokenManager.getUserIdAssociatedWithToken(token));
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        SubmittedCourseWorkInfo submittedCourseWorkInfo;
        if (serviceResult.isSuccess()) {
            submittedCourseWorkInfo = ProtoUtils.generateSubmittedCourseWorkInfo(
                    serviceResult.extra(SubmittedCourseWork.class), serviceResult.extra(User.class));
        } else {
            submittedCourseWorkInfo = ProtoUtils.generateSubmittedCourseWorkInfo(submittedCourseWork,
                    userService.retrieve(submittedCourseWork.getSubmitterId()).orElse(null));
        }
        return SubmittedCourseWorkUpdateResponse.newBuilder()
                .setResponse(baseResponse)
                .setSubmittedCourseWorkInfo(submittedCourseWorkInfo)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/findAll", produces = "application/x-protobuf", method = RequestMethod.POST)
    public SubmittedCourseWorkFindAllResponse findAllSubmittedCourseWork(@RequestBody SubmittedCourseWorkFindAllRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return SubmittedCourseWorkFindAllResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        ServiceResult serviceResult = submittedCourseWorkService.findAllSubmittedCourseWork(request.getBelongCourseWorkId());
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        List<SubmittedCourseWorkSimpleInfo> submittedCourseWorkSimpleInfos = new LinkedList<>();
        for (SubmittedCourseWork submittedCourseWork : (List<SubmittedCourseWork>) serviceResult.extra(List.class)) {
            submittedCourseWorkSimpleInfos.add(ProtoUtils.generateSubmittedCourseWorkSimpleInfo(
                    submittedCourseWork, userService.retrieve(submittedCourseWork.getSubmitterId()).orElse(null)));
        }
        return SubmittedCourseWorkFindAllResponse.newBuilder()
                .setResponse(baseResponse)
                .addAllSubmittedCourseWorkSimpleInfo(submittedCourseWorkSimpleInfos)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/get", produces = "application/x-protobuf", method = RequestMethod.POST)
    public SubmittedCourseWorkGetResponse getSubmittedCourseWork(@RequestBody SubmittedCourseWorkGetRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return SubmittedCourseWorkGetResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Optional<SubmittedCourseWork> submittedCourseWorkOptional;
        if (request.getById()) {
            submittedCourseWorkOptional = submittedCourseWorkService.retrieve(request.getSubmittedCourseWorkId());
        } else {
            submittedCourseWorkOptional = submittedCourseWorkService
                    .retrieveByBelongCourseWorkIdAndSubmitterId(request.getBelongCourseWorkId(), request.getSubmitterId());
        }
        if (!submittedCourseWorkOptional.isPresent()) {
            baseResponse = ControllerUtils.generateErrorBaseResponse(token, "no submitted course work exists");
        } else {
            baseResponse = ControllerUtils.generateSuccessBaseResponse(token, "get submitted course work success");
        }
        Long submitterId = submittedCourseWorkOptional.map(SubmittedCourseWork::getSubmitterId).orElse(null);
        return SubmittedCourseWorkGetResponse.newBuilder()
                .setResponse(baseResponse)
                .setSubmittedCourseWorkInfo(ProtoUtils.generateSubmittedCourseWorkInfo(
                        submittedCourseWorkOptional.orElse(null), userService.retrieve(submitterId).orElse(null)))
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/delete", produces = "application/x-protobuf", method = RequestMethod.POST)
    public SubmittedCourseWorkDeleteResponse deleteSubmittedCourseWork(@RequestBody SubmittedCourseWorkDeleteRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return SubmittedCourseWorkDeleteResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        ServiceResult serviceResult = submittedCourseWorkService.deleteSubmittedCourseWork(request.getSubmittedCourseWorkId());
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        return SubmittedCourseWorkDeleteResponse.newBuilder()
                .setResponse(baseResponse)
                .setSubmittedCourseWorkId(request.getSubmittedCourseWorkId())
                .build();
    }

}
