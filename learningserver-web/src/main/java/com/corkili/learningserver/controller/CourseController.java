package com.corkili.learningserver.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.corkili.learningserver.bo.Course;
import com.corkili.learningserver.bo.User;
import com.corkili.learningserver.common.ControllerUtils;
import com.corkili.learningserver.common.ImageUtils;
import com.corkili.learningserver.common.ProtoUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.generate.protobuf.Info.CourseInfo;
import com.corkili.learningserver.generate.protobuf.Info.Image;
import com.corkili.learningserver.generate.protobuf.Request.CourseCreateRequest;
import com.corkili.learningserver.generate.protobuf.Response.BaseResponse;
import com.corkili.learningserver.generate.protobuf.Response.CourseCreateResponse;
import com.corkili.learningserver.service.CourseService;
import com.corkili.learningserver.service.UserService;
import com.corkili.learningserver.token.TokenManager;

@Controller
@RequestMapping("/course")
public class CourseController {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/createCourse", produces = "application/x-protobuf", method = RequestMethod.POST)
    public CourseCreateResponse createCourse(@RequestBody CourseCreateRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return CourseCreateResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Long teacherId = tokenManager.getUserIdAssociatedWithToken(token);
        Optional<User> userOptional = userService.retrieve(teacherId);
        if (!userOptional.isPresent()) {
            return CourseCreateResponse.newBuilder()
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "user info not found"))
                    .build();
        }
        User teacher = userOptional.get();
        Course course = new Course();
        course.setCourseName(request.getCourseName());
        course.setDescription(request.getDescription());
        Map<String, byte[]> images = new HashMap<>();
        for (Image image : request.getImageList()) {
            if (image.getHasData()) {
                images.put(ImageUtils.getImagePath(image.getPath(), teacherId), image.getImage().toByteArray());
            }
        }
        for (String tag : request.getTagList()) {
            course.addTag(tag);
        }
        course.setTeacherId(teacherId);
        ServiceResult serviceResult = courseService.createCourse(course, images);
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        CourseInfo courseInfo;
        if (serviceResult.isSuccess()) {
            courseInfo = ProtoUtils.generateCourseInfo((Course) serviceResult.extra(Course.class), teacher, false);
        } else {
            courseInfo = ProtoUtils.generateCourseInfo(course, teacher, false);
        }
        return CourseCreateResponse.newBuilder()
                .setResponse(baseResponse)
                .setCourseInfo(courseInfo)
                .build();
    }

}
