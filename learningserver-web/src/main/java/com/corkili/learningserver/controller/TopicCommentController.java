package com.corkili.learningserver.controller;

import com.corkili.learningserver.bo.TopicComment;
import com.corkili.learningserver.bo.User;
import com.corkili.learningserver.common.ControllerUtils;
import com.corkili.learningserver.common.ProtoUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.generate.protobuf.Info.TopicCommentInfo;
import com.corkili.learningserver.generate.protobuf.Request.TopicCommentCreateRequest;
import com.corkili.learningserver.generate.protobuf.Request.TopicCommentDeleteRequest;
import com.corkili.learningserver.generate.protobuf.Request.TopicCommentFindAllRequest;
import com.corkili.learningserver.generate.protobuf.Request.TopicCommentGetRequest;
import com.corkili.learningserver.generate.protobuf.Request.TopicCommentUpdateRequest;
import com.corkili.learningserver.generate.protobuf.Response.BaseResponse;
import com.corkili.learningserver.generate.protobuf.Response.TopicCommentCreateResponse;
import com.corkili.learningserver.generate.protobuf.Response.TopicCommentDeleteResponse;
import com.corkili.learningserver.generate.protobuf.Response.TopicCommentFindAllResponse;
import com.corkili.learningserver.generate.protobuf.Response.TopicCommentGetResponse;
import com.corkili.learningserver.generate.protobuf.Response.TopicCommentUpdateResponse;
import com.corkili.learningserver.service.TopicCommentService;
import com.corkili.learningserver.service.UserService;
import com.corkili.learningserver.token.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/topicComment")
public class TopicCommentController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private TopicCommentService topicCommentService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/create", produces = "application/x-protobuf", method = RequestMethod.POST)
    public TopicCommentCreateResponse createTopicComment(@RequestBody TopicCommentCreateRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return TopicCommentCreateResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Long authorId = tokenManager.getUserIdAssociatedWithToken(token);
        Optional<User> authorOptional = userService.retrieve(authorId);
        if (!authorOptional.isPresent()) {
            return TopicCommentCreateResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "author info not found"))
                    .build();
        }
        User author = authorOptional.get();
        TopicComment topicComment = new TopicComment();
        topicComment.setContent(request.getContent());
        Map<String, byte[]> images = ControllerUtils.generateImageMap("topicComment", authorId, request.getImageList());
        topicComment.setAuthorId(authorId);
        topicComment.setBelongTopicId(request.getBelongTopicId());
        ServiceResult serviceResult = topicCommentService.createTopicComment(topicComment, images);
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        TopicCommentInfo topicCommentInfo;
        if (serviceResult.isSuccess()) {
            topicCommentInfo = ProtoUtils.generateTopicCommentInfo(serviceResult.extra(TopicComment.class), author, false);
        } else {
            topicCommentInfo = ProtoUtils.generateTopicCommentInfo(topicComment, author, false);
        }
        return TopicCommentCreateResponse.newBuilder()
                .setResponse(baseResponse)
                .setTopicCommentInfo(topicCommentInfo)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/update", produces = "application/x-protobuf", method = RequestMethod.POST)
    public TopicCommentUpdateResponse updateTopicComment(@RequestBody TopicCommentUpdateRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return TopicCommentUpdateResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Long authorId = tokenManager.getUserIdAssociatedWithToken(token);
        Optional<TopicComment> topicCommentOptional = topicCommentService.retrieve(request.getTopicCommentId());
        if (!topicCommentOptional.isPresent()) {
            return TopicCommentUpdateResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "topicComment not found"))
                    .build();
        }
        Optional<User> authorOptional = userService.retrieve(authorId);
        if (!authorOptional.isPresent()) {
            return TopicCommentUpdateResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "author info not found"))
                    .build();
        }
        User author = authorOptional.get();
        TopicComment topicComment = TopicComment.copyFrom(topicCommentOptional.get());
        if (request.getUpdateContent()) {
            topicComment.setContent(topicComment.getContent());
        }
        Map<String, byte[]> images = null;
        if (request.getUpdateImage()) {
            images = ControllerUtils.generateImageMap("topicComment", authorId, request.getImageList());
        }
        ServiceResult serviceResult = topicCommentService.updateTopicComment(topicComment, images);
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        TopicCommentInfo topicCommentInfo;
        if (serviceResult.isSuccess()) {
            topicCommentInfo = ProtoUtils.generateTopicCommentInfo(serviceResult.extra(TopicComment.class), author, false);
        } else {
            topicCommentInfo = ProtoUtils.generateTopicCommentInfo(topicComment, author, false);
        }
        return TopicCommentUpdateResponse.newBuilder()
                .setResponse(baseResponse)
                .setTopicCommentInfo(topicCommentInfo)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/findAll", produces = "application/x-protobuf", method = RequestMethod.POST)
    public TopicCommentFindAllResponse findAllTopicComment(@RequestBody TopicCommentFindAllRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return TopicCommentFindAllResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        ServiceResult serviceResult = topicCommentService.findAllTopicComment(request.getBelongTopicId());
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        List<TopicCommentInfo> topicCommentInfoList = new LinkedList<>();
        if (serviceResult.isSuccess()) {
            List<TopicComment> topicCommentList = (List<TopicComment>) serviceResult.extra(List.class);
            for (TopicComment topicComment : topicCommentList) {
                Optional<User> authorOptional = userService.retrieve(topicComment.getAuthorId());
                authorOptional.ifPresent(user -> topicCommentInfoList
                        .add(ProtoUtils.generateTopicCommentInfo(topicComment, user, false)));
            }
        }
        return TopicCommentFindAllResponse.newBuilder()
                .setResponse(baseResponse)
                .addAllTopicCommentInfo(topicCommentInfoList)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/get", produces = "application/x-protobuf", method = RequestMethod.POST)
    public TopicCommentGetResponse getTopicComment(@RequestBody TopicCommentGetRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return TopicCommentGetResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Optional<TopicComment> topicCommentOptional = topicCommentService.retrieve(request.getTopicCommentId());
        if (!topicCommentOptional.isPresent()) {
            return TopicCommentGetResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "topicComment not found"))
                    .build();
        }
        TopicComment topicComment = topicCommentOptional.get();
        Optional<User> authorOptional = userService.retrieve(topicComment.getAuthorId());
        if (!authorOptional.isPresent()) {
            return TopicCommentGetResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "author info not found"))
                    .build();
        }
        return TopicCommentGetResponse.newBuilder()
                .setResponse(ControllerUtils.generateSuccessBaseResponse(token, "get topicComment success"))
                .setTopicCommentInfo(ProtoUtils.generateTopicCommentInfo(topicComment, authorOptional.get(), false))
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/delete", produces = "application/x-protobuf", method = RequestMethod.POST)
    public TopicCommentDeleteResponse deleteCourse(@RequestBody TopicCommentDeleteRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return TopicCommentDeleteResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        ServiceResult serviceResult = topicCommentService.deleteTopicComment(request.getTopicCommentId());
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        return TopicCommentDeleteResponse.newBuilder()
                .setResponse(baseResponse)
                .setTopicCommentId(request.getTopicCommentId())
                .build();
    }


}
