package com.corkili.learningserver.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.corkili.learningserver.bo.Course;
import com.corkili.learningserver.bo.CourseSubscription;
import com.corkili.learningserver.bo.User;
import com.corkili.learningserver.common.ControllerUtils;
import com.corkili.learningserver.common.ProtoUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.generate.protobuf.Info.CourseSubscriptionInfo;
import com.corkili.learningserver.generate.protobuf.Request.CourseSubscriptionCreateRequest;
import com.corkili.learningserver.generate.protobuf.Request.CourseSubscriptionDeleteRequest;
import com.corkili.learningserver.generate.protobuf.Request.CourseSubscriptionFindAllRequest;
import com.corkili.learningserver.generate.protobuf.Response.BaseResponse;
import com.corkili.learningserver.generate.protobuf.Response.CourseSubscriptionCreateResponse;
import com.corkili.learningserver.generate.protobuf.Response.CourseSubscriptionDeleteResponse;
import com.corkili.learningserver.generate.protobuf.Response.CourseSubscriptionFindAllResponse;
import com.corkili.learningserver.service.CourseService;
import com.corkili.learningserver.service.CourseSubscriptionService;
import com.corkili.learningserver.service.UserService;
import com.corkili.learningserver.token.TokenManager;

@Controller
@RequestMapping("/courseSubscription")
public class CourseSubscriptionController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private CourseSubscriptionService courseSubscriptionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @ResponseBody
    @RequestMapping(value = "/create", produces = "application/x-protobuf", method = RequestMethod.POST)
    public CourseSubscriptionCreateResponse createCourseSubscription(@RequestBody CourseSubscriptionCreateRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return CourseSubscriptionCreateResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Long subscriberId = tokenManager.getUserIdAssociatedWithToken(token);
        Optional<User> subscriberOptional = userService.retrieve(subscriberId);
        if (!subscriberOptional.isPresent()) {
            return CourseSubscriptionCreateResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "subscriber info not found"))
                    .build();
        }
        User subscriber = subscriberOptional.get();
        CourseSubscription courseSubscription = new CourseSubscription();
        courseSubscription.setSubscriberId(subscriberId);
        courseSubscription.setSubscribedCourseId(request.getSubscribedCourseId());
        ServiceResult serviceResult = courseSubscriptionService.createCourseSubscription(courseSubscription);
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        CourseSubscriptionInfo courseSubscriptionInfo;
        if (serviceResult.isSuccess()) {
            courseSubscription = serviceResult.extra(CourseSubscription.class);
            Course course = courseService.retrieve(courseSubscription.getSubscribedCourseId()).orElse(null);
            User teacher = userService.retrieve(course != null ? course.getTeacherId() : null).orElse(null);
            courseSubscriptionInfo = ProtoUtils.generateCourseSubscriptionInfo(courseSubscription, subscriber, course, teacher, false);
        } else {
            courseSubscriptionInfo = ProtoUtils.generateCourseSubscriptionInfo(courseSubscription, subscriber, null, null, false);
        }
        return CourseSubscriptionCreateResponse.newBuilder()
                .setResponse(baseResponse)
                .setCourseSubscriptionInfo(courseSubscriptionInfo)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/findAll", produces = "application/x-protobuf", method = RequestMethod.POST)
    public CourseSubscriptionFindAllResponse findAllCourseSubscription(@RequestBody CourseSubscriptionFindAllRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return CourseSubscriptionFindAllResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Long subscriberId = null;
        if (request.getBySubscriberId()) {
            if (request.getSubscriberId() < 0) {
                subscriberId = tokenManager.getUserIdAssociatedWithToken(token);
            } else {
                subscriberId = request.getSubscriberId();
            }
        }
        Long subscribedCourseId = request.getBySubscribedCourseId() ? request.getSubscribedCourseId() : null;
        ServiceResult serviceResult = courseSubscriptionService.findAllCourseSubscription(subscriberId, subscribedCourseId);
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        List<CourseSubscriptionInfo> courseSubscriptionInfoList = new LinkedList<>();
        if (serviceResult.isSuccess()) {
            List<CourseSubscription> courseSubscriptionList = (List<CourseSubscription>) serviceResult.extra(List.class);
            for (CourseSubscription courseSubscription : courseSubscriptionList) {
                Optional<User> subscriberOptional = userService.retrieve(courseSubscription.getSubscriberId());
                Optional<Course> courseOptional = courseService.retrieve(courseSubscription.getSubscribedCourseId());
                Optional<User> teacherOptional = userService.retrieve(courseOptional.map(Course::getTeacherId).orElse(null));
                if (subscriberOptional.isPresent() && courseOptional.isPresent() && teacherOptional.isPresent()) {
                    courseSubscriptionInfoList.add(ProtoUtils.generateCourseSubscriptionInfo(courseSubscription,
                            subscriberOptional.get(), courseOptional.get(), teacherOptional.get(), false));
                }
            }
        }
        return CourseSubscriptionFindAllResponse.newBuilder()
                .setResponse(baseResponse)
                .addAllCourseSubscriptionInfo(courseSubscriptionInfoList)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/delete", produces = "application/x-protobuf", method = RequestMethod.POST)
    public CourseSubscriptionDeleteResponse deleteCourseSubscription(@RequestBody CourseSubscriptionDeleteRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return CourseSubscriptionDeleteResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        ServiceResult serviceResult = courseSubscriptionService.deleteCourseSubscription(request.getCourseSubscriptionId());
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        return CourseSubscriptionDeleteResponse.newBuilder()
                .setResponse(baseResponse)
                .setCourseSubscriptionId(request.getCourseSubscriptionId())
                .build();
    }

}
