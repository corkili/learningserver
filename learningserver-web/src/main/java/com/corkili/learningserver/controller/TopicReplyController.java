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

import com.corkili.learningserver.bo.TopicReply;
import com.corkili.learningserver.bo.User;
import com.corkili.learningserver.common.ControllerUtils;
import com.corkili.learningserver.common.ProtoUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.generate.protobuf.Info.TopicReplyInfo;
import com.corkili.learningserver.generate.protobuf.Request.TopicReplyCreateRequest;
import com.corkili.learningserver.generate.protobuf.Request.TopicReplyDeleteRequest;
import com.corkili.learningserver.generate.protobuf.Request.TopicReplyFindAllRequest;
import com.corkili.learningserver.generate.protobuf.Request.TopicReplyGetRequest;
import com.corkili.learningserver.generate.protobuf.Request.TopicReplyUpdateRequest;
import com.corkili.learningserver.generate.protobuf.Response.BaseResponse;
import com.corkili.learningserver.generate.protobuf.Response.TopicReplyCreateResponse;
import com.corkili.learningserver.generate.protobuf.Response.TopicReplyDeleteResponse;
import com.corkili.learningserver.generate.protobuf.Response.TopicReplyFindAllResponse;
import com.corkili.learningserver.generate.protobuf.Response.TopicReplyGetResponse;
import com.corkili.learningserver.generate.protobuf.Response.TopicReplyUpdateResponse;
import com.corkili.learningserver.service.TopicReplyService;
import com.corkili.learningserver.service.UserService;
import com.corkili.learningserver.token.TokenManager;

@Controller
@RequestMapping("/topicReply")
public class TopicReplyController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private TopicReplyService topicReplyService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/create", produces = "application/x-protobuf", method = RequestMethod.POST)
    public TopicReplyCreateResponse createTopicReply(@RequestBody TopicReplyCreateRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return TopicReplyCreateResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Long authorId = tokenManager.getUserIdAssociatedWithToken(token);
        Optional<User> authorOptional = userService.retrieve(authorId);
        if (!authorOptional.isPresent()) {
            return TopicReplyCreateResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "author info not found"))
                    .build();
        }
        User author = authorOptional.get();
        TopicReply topicReply = new TopicReply();
        topicReply.setContent(request.getContent());
        Map<String, byte[]> images = ControllerUtils.generateImageMap("topicReply", authorId, request.getImageList());
        topicReply.setAuthorId(authorId);
        topicReply.setBelongCommentId(request.getBelongCommentId());
        ServiceResult serviceResult = topicReplyService.createTopicReply(topicReply, images);
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        TopicReplyInfo topicReplyInfo;
        if (serviceResult.isSuccess()) {
            topicReplyInfo = ProtoUtils.generateTopicReplyInfo(serviceResult.extra(TopicReply.class), author, false);
        } else {
            topicReplyInfo = ProtoUtils.generateTopicReplyInfo(topicReply, author, false);
        }
        return TopicReplyCreateResponse.newBuilder()
                .setResponse(baseResponse)
                .setTopicReplyInfo(topicReplyInfo)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/update", produces = "application/x-protobuf", method = RequestMethod.POST)
    public TopicReplyUpdateResponse updateTopicReply(@RequestBody TopicReplyUpdateRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return TopicReplyUpdateResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Long authorId = tokenManager.getUserIdAssociatedWithToken(token);
        Optional<TopicReply> topicReplyOptional = topicReplyService.retrieve(request.getTopicReplyId());
        if (!topicReplyOptional.isPresent()) {
            return TopicReplyUpdateResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "topicReply not found"))
                    .build();
        }
        Optional<User> authorOptional = userService.retrieve(authorId);
        if (!authorOptional.isPresent()) {
            return TopicReplyUpdateResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "author info not found"))
                    .build();
        }
        User author = authorOptional.get();
        TopicReply topicReply = TopicReply.copyFrom(topicReplyOptional.get());
        if (request.getUpdateContent()) {
            topicReply.setContent(request.getContent());
        }
        Map<String, byte[]> images = null;
        if (request.getUpdateImage()) {
            images = ControllerUtils.generateImageMap("topicReply", authorId, request.getImageList());
        }
        ServiceResult serviceResult = topicReplyService.updateTopicReply(topicReply, images);
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        TopicReplyInfo topicReplyInfo;
        if (serviceResult.isSuccess()) {
            topicReplyInfo = ProtoUtils.generateTopicReplyInfo(serviceResult.extra(TopicReply.class), author, false);
        } else {
            topicReplyInfo = ProtoUtils.generateTopicReplyInfo(topicReply, author, false);
        }
        return TopicReplyUpdateResponse.newBuilder()
                .setResponse(baseResponse)
                .setTopicReplyInfo(topicReplyInfo)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/findAll", produces = "application/x-protobuf", method = RequestMethod.POST)
    public TopicReplyFindAllResponse findAllTopicReply(@RequestBody TopicReplyFindAllRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return TopicReplyFindAllResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        ServiceResult serviceResult = topicReplyService.findAllTopicReply(request.getBelongCommentId());
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        List<TopicReplyInfo> topicReplyInfoList = new LinkedList<>();
        if (serviceResult.isSuccess()) {
            List<TopicReply> topicReplyList = (List<TopicReply>) serviceResult.extra(List.class);
            for (TopicReply topicReply : topicReplyList) {
                Optional<User> authorOptional = userService.retrieve(topicReply.getAuthorId());
                authorOptional.ifPresent(user -> topicReplyInfoList
                        .add(ProtoUtils.generateTopicReplyInfo(topicReply, user, false)));
            }
        }
        return TopicReplyFindAllResponse.newBuilder()
                .setResponse(baseResponse)
                .addAllTopicReplyInfo(topicReplyInfoList)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/get", produces = "application/x-protobuf", method = RequestMethod.POST)
    public TopicReplyGetResponse getTopicReply(@RequestBody TopicReplyGetRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return TopicReplyGetResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Optional<TopicReply> topicReplyOptional = topicReplyService.retrieve(request.getTopicReplyId());
        if (!topicReplyOptional.isPresent()) {
            return TopicReplyGetResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "topicReply not found"))
                    .build();
        }
        TopicReply topicReply = topicReplyOptional.get();
        Optional<User> authorOptional = userService.retrieve(topicReply.getAuthorId());
        if (!authorOptional.isPresent()) {
            return TopicReplyGetResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "author info not found"))
                    .build();
        }
        return TopicReplyGetResponse.newBuilder()
                .setResponse(ControllerUtils.generateSuccessBaseResponse(token, "get topicReply success"))
                .setTopicReplyInfo(ProtoUtils.generateTopicReplyInfo(topicReply, authorOptional.get(), false))
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/delete", produces = "application/x-protobuf", method = RequestMethod.POST)
    public TopicReplyDeleteResponse deleteCourse(@RequestBody TopicReplyDeleteRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return TopicReplyDeleteResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        ServiceResult serviceResult = topicReplyService.deleteTopicReply(request.getTopicReplyId());
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        return TopicReplyDeleteResponse.newBuilder()
                .setResponse(baseResponse)
                .setTopicReplyId(request.getTopicReplyId())
                .build();
    }
    
}
