package com.corkili.learningserver.controller;

import com.corkili.learningserver.bo.ForumTopic;
import com.corkili.learningserver.bo.User;
import com.corkili.learningserver.common.ControllerUtils;
import com.corkili.learningserver.common.ProtoUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.generate.protobuf.Info.ForumTopicInfo;
import com.corkili.learningserver.generate.protobuf.Request.ForumTopicCreateRequest;
import com.corkili.learningserver.generate.protobuf.Request.ForumTopicDeleteRequest;
import com.corkili.learningserver.generate.protobuf.Request.ForumTopicFindAllRequest;
import com.corkili.learningserver.generate.protobuf.Request.ForumTopicGetRequest;
import com.corkili.learningserver.generate.protobuf.Request.ForumTopicUpdateRequest;
import com.corkili.learningserver.generate.protobuf.Response.BaseResponse;
import com.corkili.learningserver.generate.protobuf.Response.ForumTopicCreateResponse;
import com.corkili.learningserver.generate.protobuf.Response.ForumTopicDeleteResponse;
import com.corkili.learningserver.generate.protobuf.Response.ForumTopicFindAllResponse;
import com.corkili.learningserver.generate.protobuf.Response.ForumTopicGetResponse;
import com.corkili.learningserver.generate.protobuf.Response.ForumTopicUpdateResponse;
import com.corkili.learningserver.service.ForumTopicService;
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
@RequestMapping("/forumTopic")
public class ForumTopicController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private ForumTopicService forumTopicService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/create", produces = "application/x-protobuf", method = RequestMethod.POST)
    public ForumTopicCreateResponse createForumTopic(@RequestBody ForumTopicCreateRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return ForumTopicCreateResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Long authorId = tokenManager.getUserIdAssociatedWithToken(token);
        Optional<User> authorOptional = userService.retrieve(authorId);
        if (!authorOptional.isPresent()) {
            return ForumTopicCreateResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "author info not found"))
                    .build();
        }
        User author = authorOptional.get();
        ForumTopic forumTopic = new ForumTopic();
        forumTopic.setTitle(request.getTitle());
        forumTopic.setDescription(request.getDescription());
        Map<String, byte[]> images = ControllerUtils.generateImageMap("forumTopic", authorId, request.getImageList());
        forumTopic.setAuthorId(authorId);
        forumTopic.setBelongCourseId(request.getBelongCourseId());
        ServiceResult serviceResult = forumTopicService.createForumTopic(forumTopic, images);
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        ForumTopicInfo forumTopicInfo;
        if (serviceResult.isSuccess()) {
            forumTopicInfo = ProtoUtils.generateForumTopicInfo(serviceResult.extra(ForumTopic.class), author, false);
        } else {
            forumTopicInfo = ProtoUtils.generateForumTopicInfo(forumTopic, author, false);
        }
        return ForumTopicCreateResponse.newBuilder()
                .setResponse(baseResponse)
                .setForumTopicInfo(forumTopicInfo)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/update", produces = "application/x-protobuf", method = RequestMethod.POST)
    public ForumTopicUpdateResponse updateForumTopic(@RequestBody ForumTopicUpdateRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return ForumTopicUpdateResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Long authorId = tokenManager.getUserIdAssociatedWithToken(token);
        Optional<ForumTopic> forumTopicOptional = forumTopicService.retrieve(request.getForumTopicId());
        if (!forumTopicOptional.isPresent()) {
            return ForumTopicUpdateResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "forumTopic not found"))
                    .build();
        }
        Optional<User> authorOptional = userService.retrieve(authorId);
        if (!authorOptional.isPresent()) {
            return ForumTopicUpdateResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "author info not found"))
                    .build();
        }
        User author = authorOptional.get();
        ForumTopic forumTopic = ForumTopic.copyFrom(forumTopicOptional.get());
        if (request.getUpdateDescription()) {
            forumTopic.setDescription(forumTopic.getDescription());
        }
        Map<String, byte[]> images = null;
        if (request.getUpdateImage()) {
            images = ControllerUtils.generateImageMap("forumTopic", authorId, request.getImageList());
        }
        ServiceResult serviceResult = forumTopicService.updateForumTopic(forumTopic, images);
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        ForumTopicInfo forumTopicInfo;
        if (serviceResult.isSuccess()) {
            forumTopicInfo = ProtoUtils.generateForumTopicInfo(serviceResult.extra(ForumTopic.class), author, false);
        } else {
            forumTopicInfo = ProtoUtils.generateForumTopicInfo(forumTopic, author, false);
        }
        return ForumTopicUpdateResponse.newBuilder()
                .setResponse(baseResponse)
                .setForumTopicInfo(forumTopicInfo)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/findAll", produces = "application/x-protobuf", method = RequestMethod.POST)
    public ForumTopicFindAllResponse findAllForumTopic(@RequestBody ForumTopicFindAllRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return ForumTopicFindAllResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        ServiceResult serviceResult = forumTopicService.findAllForumTopic(request.getBelongCourseId());
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        List<ForumTopicInfo> forumTopicInfoList = new LinkedList<>();
        if (serviceResult.isSuccess()) {
            List<ForumTopic> forumTopicList = (List<ForumTopic>) serviceResult.extra(List.class);
            for (ForumTopic forumTopic : forumTopicList) {
                Optional<User> authorOptional = userService.retrieve(forumTopic.getAuthorId());
                authorOptional.ifPresent(user -> forumTopicInfoList
                        .add(ProtoUtils.generateForumTopicInfo(forumTopic, user, false)));
            }
        }
        return ForumTopicFindAllResponse.newBuilder()
                .setResponse(baseResponse)
                .addAllForumTopicInfo(forumTopicInfoList)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/get", produces = "application/x-protobuf", method = RequestMethod.POST)
    public ForumTopicGetResponse getForumTopic(@RequestBody ForumTopicGetRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return ForumTopicGetResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Optional<ForumTopic> forumTopicOptional = forumTopicService.retrieve(request.getForumTopicId());
        if (!forumTopicOptional.isPresent()) {
            return ForumTopicGetResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "forumTopic not found"))
                    .build();
        }
        ForumTopic forumTopic = forumTopicOptional.get();
        Optional<User> authorOptional = userService.retrieve(forumTopic.getAuthorId());
        if (!authorOptional.isPresent()) {
            return ForumTopicGetResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "author info not found"))
                    .build();
        }
        return ForumTopicGetResponse.newBuilder()
                .setResponse(ControllerUtils.generateSuccessBaseResponse(token, "get forumTopic success"))
                .setForumTopicInfo(ProtoUtils.generateForumTopicInfo(forumTopic, authorOptional.get(), false))
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/delete", produces = "application/x-protobuf", method = RequestMethod.POST)
    public ForumTopicDeleteResponse deleteCourse(@RequestBody ForumTopicDeleteRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return ForumTopicDeleteResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        ServiceResult serviceResult = forumTopicService.deleteForumTopic(request.getForumTopicId());
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        return ForumTopicDeleteResponse.newBuilder()
                .setResponse(baseResponse)
                .setForumTopicId(request.getForumTopicId())
                .build();
    }

}
