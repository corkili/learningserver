package com.corkili.learningserver.controller;

import com.corkili.learningserver.bo.CourseComment;
import com.corkili.learningserver.bo.User;
import com.corkili.learningserver.common.ControllerUtils;
import com.corkili.learningserver.common.ProtoUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.generate.protobuf.Info.CourseCommentInfo;
import com.corkili.learningserver.generate.protobuf.Request.CourseCommentCreateRequest;
import com.corkili.learningserver.generate.protobuf.Request.CourseCommentDeleteRequest;
import com.corkili.learningserver.generate.protobuf.Request.CourseCommentFindAllRequest;
import com.corkili.learningserver.generate.protobuf.Request.CourseCommentGetRequest;
import com.corkili.learningserver.generate.protobuf.Request.CourseCommentUpdateRequest;
import com.corkili.learningserver.generate.protobuf.Response.BaseResponse;
import com.corkili.learningserver.generate.protobuf.Response.CourseCommentCreateResponse;
import com.corkili.learningserver.generate.protobuf.Response.CourseCommentDeleteResponse;
import com.corkili.learningserver.generate.protobuf.Response.CourseCommentFindAllResponse;
import com.corkili.learningserver.generate.protobuf.Response.CourseCommentGetResponse;
import com.corkili.learningserver.generate.protobuf.Response.CourseCommentUpdateResponse;
import com.corkili.learningserver.service.CourseCommentService;
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
@RequestMapping("/courseComment")
public class CourseCommentController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private CourseCommentService courseCommentService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/create", produces = "application/x-protobuf", method = RequestMethod.POST)
    public CourseCommentCreateResponse createCourseComment(@RequestBody CourseCommentCreateRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return CourseCommentCreateResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Long commentAuthorId = tokenManager.getUserIdAssociatedWithToken(token);
        Optional<User> commentedAuthorOptional = userService.retrieve(commentAuthorId);
        if (!commentedAuthorOptional.isPresent()) {
            return CourseCommentCreateResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "commentedAuthor info not found"))
                    .build();
        }
        User commentedAuthor = commentedAuthorOptional.get();
        CourseComment courseComment = new CourseComment();
        courseComment.setCommentType(CourseComment.Type.valueOf(request.getCommentType().name()));
        courseComment.setContent(request.getContent());
        Map<String, byte[]> images = ControllerUtils.generateImageMap("courseComment", commentAuthorId, request.getImageList());
        courseComment.setCommentAuthorId(commentAuthorId);
        courseComment.setCommentedCourseId(request.getCommentedCourseId());
        ServiceResult serviceResult = courseCommentService.createCourseComment(courseComment, images);
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        CourseCommentInfo courseCommentInfo;
        if (serviceResult.isSuccess()) {
            courseCommentInfo = ProtoUtils.generateCourseCommentInfo(serviceResult.extra(CourseComment.class), commentedAuthor, false);
        } else {
            courseCommentInfo = ProtoUtils.generateCourseCommentInfo(courseComment, commentedAuthor, false);
        }
        return CourseCommentCreateResponse.newBuilder()
                .setResponse(baseResponse)
                .setCourseCommentInfo(courseCommentInfo)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/update", produces = "application/x-protobuf", method = RequestMethod.POST)
    public CourseCommentUpdateResponse updateCourseComment(@RequestBody CourseCommentUpdateRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return CourseCommentUpdateResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Long commentAuthorId = tokenManager.getUserIdAssociatedWithToken(token);
        Optional<CourseComment> courseCommentOptional = courseCommentService.retrieve(request.getCourseCommentId());
        if (!courseCommentOptional.isPresent()) {
            return CourseCommentUpdateResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "courseComment not found"))
                    .build();
        }
        Optional<User> commentedAuthorOptional = userService.retrieve(commentAuthorId);
        if (!commentedAuthorOptional.isPresent()) {
            return CourseCommentUpdateResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "commentedAuthor info not found"))
                    .build();
        }
        User commentedAuthor = commentedAuthorOptional.get();
        CourseComment courseComment = CourseComment.copyFrom(courseCommentOptional.get());
        if (request.getUpdateContent()) {
            courseComment.setContent(courseComment.getContent());
        }
        Map<String, byte[]> images = null;
        if (request.getUpdateImage()) {
            images = ControllerUtils.generateImageMap("courseComment", commentAuthorId, request.getImageList());
        }
        ServiceResult serviceResult = courseCommentService.updateCourseComment(courseComment, images);
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        CourseCommentInfo courseCommentInfo;
        if (serviceResult.isSuccess()) {
            courseCommentInfo = ProtoUtils.generateCourseCommentInfo(serviceResult.extra(CourseComment.class), commentedAuthor, false);
        } else {
            courseCommentInfo = ProtoUtils.generateCourseCommentInfo(courseComment, commentedAuthor, false);
        }
        return CourseCommentUpdateResponse.newBuilder()
                .setResponse(baseResponse)
                .setCourseCommentInfo(courseCommentInfo)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/findAll", produces = "application/x-protobuf", method = RequestMethod.POST)
    public CourseCommentFindAllResponse findAllCourseComment(@RequestBody CourseCommentFindAllRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return CourseCommentFindAllResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        ServiceResult serviceResult = courseCommentService.findAllCourseComment(request.getCommentedCourseId());
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        List<CourseCommentInfo> courseCommentInfoList = new LinkedList<>();
        if (serviceResult.isSuccess()) {
            List<CourseComment> courseCommentList = (List<CourseComment>) serviceResult.extra(List.class);
            for (CourseComment courseComment : courseCommentList) {
                Optional<User> commentedAuthorOptional = userService.retrieve(courseComment.getCommentAuthorId());
                commentedAuthorOptional.ifPresent(user -> courseCommentInfoList
                        .add(ProtoUtils.generateCourseCommentInfo(courseComment, user, false)));
            }
        }
        return CourseCommentFindAllResponse.newBuilder()
                .setResponse(baseResponse)
                .addAllCourseCommentInfo(courseCommentInfoList)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/get", produces = "application/x-protobuf", method = RequestMethod.POST)
    public CourseCommentGetResponse getCourseComment(@RequestBody CourseCommentGetRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return CourseCommentGetResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Optional<CourseComment> courseCommentOptional = courseCommentService.retrieve(request.getCourseCommentId());
        if (!courseCommentOptional.isPresent()) {
            return CourseCommentGetResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "courseComment not found"))
                    .build();
        }
        CourseComment courseComment = courseCommentOptional.get();
        Optional<User> commentedAuthorOptional = userService.retrieve(courseComment.getCommentAuthorId());
        if (!commentedAuthorOptional.isPresent()) {
            return CourseCommentGetResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "commentedAuthor info not found"))
                    .build();
        }
        return CourseCommentGetResponse.newBuilder()
                .setResponse(ControllerUtils.generateSuccessBaseResponse(token, "get courseComment success"))
                .setCourseCommentInfo(ProtoUtils.generateCourseCommentInfo(courseComment, commentedAuthorOptional.get(), false))
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/delete", produces = "application/x-protobuf", method = RequestMethod.POST)
    public CourseCommentDeleteResponse deleteCourse(@RequestBody CourseCommentDeleteRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return CourseCommentDeleteResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        ServiceResult serviceResult = courseCommentService.deleteCourseComment(request.getCourseCommentId());
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        return CourseCommentDeleteResponse.newBuilder()
                .setResponse(baseResponse)
                .setCourseCommentId(request.getCourseCommentId())
                .build();
    }

}
