package com.corkili.learningserver.controller;

import com.corkili.learningserver.bo.SubmittedExam;
import com.corkili.learningserver.bo.User;
import com.corkili.learningserver.common.ControllerUtils;
import com.corkili.learningserver.common.ProtoUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.generate.protobuf.Info.ExamSubmittedAnswer;
import com.corkili.learningserver.generate.protobuf.Info.SubmittedAnswer;
import com.corkili.learningserver.generate.protobuf.Info.SubmittedExamInfo;
import com.corkili.learningserver.generate.protobuf.Info.SubmittedExamSimpleInfo;
import com.corkili.learningserver.generate.protobuf.Request.SubmittedExamCreateRequest;
import com.corkili.learningserver.generate.protobuf.Request.SubmittedExamDeleteRequest;
import com.corkili.learningserver.generate.protobuf.Request.SubmittedExamFindAllRequest;
import com.corkili.learningserver.generate.protobuf.Request.SubmittedExamGetRequest;
import com.corkili.learningserver.generate.protobuf.Request.SubmittedExamUpdateRequest;
import com.corkili.learningserver.generate.protobuf.Response.BaseResponse;
import com.corkili.learningserver.generate.protobuf.Response.SubmittedExamCreateResponse;
import com.corkili.learningserver.generate.protobuf.Response.SubmittedExamDeleteResponse;
import com.corkili.learningserver.generate.protobuf.Response.SubmittedExamFindAllResponse;
import com.corkili.learningserver.generate.protobuf.Response.SubmittedExamGetResponse;
import com.corkili.learningserver.generate.protobuf.Response.SubmittedExamUpdateResponse;
import com.corkili.learningserver.service.SubmittedExamService;
import com.corkili.learningserver.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.corkili.learningserver.token.TokenManager;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;

@Controller
@RequestMapping("/submittedExam")
public class SubmittedExamController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private SubmittedExamService submittedExamService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/create", produces = "application/x-protobuf", method = RequestMethod.POST)
    public SubmittedExamCreateResponse createSubmittedExam(@RequestBody SubmittedExamCreateRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return SubmittedExamCreateResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Long submitterId = tokenManager.getUserIdAssociatedWithToken(token);
        SubmittedExam submittedExam = new SubmittedExam();
        for (Entry<Integer, SubmittedAnswer> entry : request.getSubmittedAnswerMap().entrySet()) {
            submittedExam.putNewSubmittedAnswer(entry.getKey(), ProtoUtils.generateSubmittedAnswer(entry.getValue()));
        }
        submittedExam.setFinished(request.getFinished());
        submittedExam.setBelongExamId(request.getBelongExamId());
        submittedExam.setSubmitterId(submitterId);
        ServiceResult serviceResult = submittedExamService.createSubmittedExam(submittedExam);
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        SubmittedExamInfo submittedExamInfo;
        if (serviceResult.isSuccess()) {
            submittedExamInfo = ProtoUtils.generateSubmittedExamInfo(
                    serviceResult.extra(SubmittedExam.class), serviceResult.extra(User.class));
        } else {
            submittedExamInfo = ProtoUtils.generateSubmittedExamInfo(submittedExam, null);
        }
        return SubmittedExamCreateResponse.newBuilder()
                .setResponse(baseResponse)
                .setSubmittedExamInfo(submittedExamInfo)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/update", produces = "application/x-protobuf", method = RequestMethod.POST)
    public SubmittedExamUpdateResponse updateSubmittedExam(@RequestBody SubmittedExamUpdateRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return SubmittedExamUpdateResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Optional<SubmittedExam> submittedExamOptional = submittedExamService
                .retrieve(request.getSubmittedExamId());
        if (!submittedExamOptional.isPresent()) {
            return SubmittedExamUpdateResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, ServiceUtils.format(
                            "submittedExam [{}] not exists", request.getSubmittedExamId())))
                    .build();
        }
        SubmittedExam submittedExam = SubmittedExam.copyFrom(submittedExamOptional.get());
        boolean oldFinished = submittedExam.isFinished();
        if (request.getUpdateSubmittedAnswer()) {
            for (Entry<Integer, ExamSubmittedAnswer> entry : request.getSubmittedAnswerMap().entrySet()) {
                SubmittedExam.InnerSubmittedAnswer isa = submittedExam.getSubmittedAnswers().get(entry.getKey());
                isa.setSubmittedAnswer(ProtoUtils.generateSubmittedAnswer(entry.getValue().getSubmittedAnswer()));
                isa.setScore(entry.getValue().getScore());
            }
        }
        if (request.getUpdateFinished()) {
            submittedExam.setFinished(request.getFinished());
        }
        ServiceResult serviceResult = submittedExamService.updateSubmittedExam(
                submittedExam, oldFinished, tokenManager.getUserIdAssociatedWithToken(token));
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        SubmittedExamInfo submittedExamInfo;
        if (serviceResult.isSuccess()) {
            submittedExamInfo = ProtoUtils.generateSubmittedExamInfo(
                    serviceResult.extra(SubmittedExam.class), serviceResult.extra(User.class));
        } else {
            submittedExamInfo = ProtoUtils.generateSubmittedExamInfo(submittedExam,
                    userService.retrieve(submittedExam.getSubmitterId()).orElse(null));
        }
        return SubmittedExamUpdateResponse.newBuilder()
                .setResponse(baseResponse)
                .setSubmittedExamInfo(submittedExamInfo)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/findAll", produces = "application/x-protobuf", method = RequestMethod.POST)
    public SubmittedExamFindAllResponse findAllSubmittedExam(@RequestBody SubmittedExamFindAllRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return SubmittedExamFindAllResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        ServiceResult serviceResult = submittedExamService.findAllSubmittedExam(request.getBelongExamId());
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        List<SubmittedExamSimpleInfo> submittedExamSimpleInfos = new LinkedList<>();
        for (SubmittedExam submittedExam : (List<SubmittedExam>) serviceResult.extra(List.class)) {
            submittedExamSimpleInfos.add(ProtoUtils.generateSubmittedExamSimpleInfo(
                    submittedExam, userService.retrieve(submittedExam.getSubmitterId()).orElse(null)));
        }
        return SubmittedExamFindAllResponse.newBuilder()
                .setResponse(baseResponse)
                .addAllSubmittedExamSimpleInfo(submittedExamSimpleInfos)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/get", produces = "application/x-protobuf", method = RequestMethod.POST)
    public SubmittedExamGetResponse getSubmittedExam(@RequestBody SubmittedExamGetRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return SubmittedExamGetResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Optional<SubmittedExam> submittedExamOptional;
        if (request.getById()) {
            submittedExamOptional = submittedExamService.retrieve(request.getSubmittedExamId());
        } else {
            submittedExamOptional = submittedExamService
                    .retrieveByBelongExamIdAndSubmitterId(request.getBelongExamId(), request.getSubmitterId());
        }
        if (!submittedExamOptional.isPresent()) {
            baseResponse = ControllerUtils.generateErrorBaseResponse(token, "no submitted exam exists");
        }
        Long submitterId = submittedExamOptional.map(SubmittedExam::getSubmitterId).orElse(null);
        return SubmittedExamGetResponse.newBuilder()
                .setResponse(baseResponse)
                .setSubmittedExamInfo(ProtoUtils.generateSubmittedExamInfo(
                        submittedExamOptional.orElse(null), userService.retrieve(submitterId).orElse(null)))
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/delete", produces = "application/x-protobuf", method = RequestMethod.POST)
    public SubmittedExamDeleteResponse deleteSubmittedExam(@RequestBody SubmittedExamDeleteRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return SubmittedExamDeleteResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        ServiceResult serviceResult = submittedExamService.deleteSubmittedExam(request.getSubmittedExamId());
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        return SubmittedExamDeleteResponse.newBuilder()
                .setResponse(baseResponse)
                .setSubmittedExamId(request.getSubmittedExamId())
                .build();
    }

}
