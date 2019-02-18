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

import com.corkili.learningserver.bo.Course;
import com.corkili.learningserver.bo.User;
import com.corkili.learningserver.common.ControllerUtils;
import com.corkili.learningserver.common.ProtoUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.generate.protobuf.Info.CourseInfo;
import com.corkili.learningserver.generate.protobuf.Request.CourseCreateRequest;
import com.corkili.learningserver.generate.protobuf.Request.CourseDeleteRequest;
import com.corkili.learningserver.generate.protobuf.Request.CourseFindAllRequest;
import com.corkili.learningserver.generate.protobuf.Request.CourseUpdateRequest;
import com.corkili.learningserver.generate.protobuf.Response.BaseResponse;
import com.corkili.learningserver.generate.protobuf.Response.CourseCreateResponse;
import com.corkili.learningserver.generate.protobuf.Response.CourseDeleteResponse;
import com.corkili.learningserver.generate.protobuf.Response.CourseFindAllResponse;
import com.corkili.learningserver.generate.protobuf.Response.CourseUpdateResponse;
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
    @RequestMapping(value = "/create", produces = "application/x-protobuf", method = RequestMethod.POST)
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
                    .setResponse(ControllerUtils.generateErrorBaseResponse(token, "teacher info not found"))
                    .build();
        }
        User teacher = userOptional.get();
        Course course = new Course();
        course.setCourseName(request.getCourseName());
        course.setDescription(request.getDescription());
        Map<String, byte[]> images = ControllerUtils.generateImageMap("course", teacherId, request.getImageList());
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

    @ResponseBody
    @RequestMapping(value = "/findAll", produces = "application/x-protobuf", method = RequestMethod.POST)
    public CourseFindAllResponse findAllCourse(@RequestBody CourseFindAllRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return CourseFindAllResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Long teacherId = !request.getByTeacherId() ? null : request.getTeacherId();
        String teacherName = !request.getByTeacherName() ? null : request.getTeacherName();
        List<String> keywords = !request.getByKeyword() ? null : new LinkedList<>(request.getKeywordList());
        ServiceResult serviceResult = courseService.findAllCourse(request.getAll(), teacherId, teacherName, keywords);
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        List<CourseInfo> courseInfoList = new LinkedList<>();
        if (serviceResult.isSuccess()) {
            List<Course> courseList = (List<Course>) serviceResult.extra(List.class);
            for (Course course : courseList) {
                Optional<User> userOptional = userService.retrieve(course.getTeacherId());
                userOptional.ifPresent(user -> courseInfoList.add(ProtoUtils.generateCourseInfo(course, user, false)));
            }
        }
        return CourseFindAllResponse.newBuilder()
                .setResponse(baseResponse)
                .addAllCourseInfo(courseInfoList)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/update", produces = "application/x-protobuf", method = RequestMethod.POST)
    public CourseUpdateResponse updateCourse(@RequestBody CourseUpdateRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return CourseUpdateResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        Optional<Course> courseOptional = courseService.retrieve(request.getCourseId());
        CourseInfo courseInfo;
        if (!courseOptional.isPresent()) {
            baseResponse = ControllerUtils.generateErrorBaseResponse(token,
                    ServiceUtils.format("course [{}] not exist", request.getCourseId()));
            courseInfo = CourseInfo.newBuilder().build();
        } else {
            Optional<User> userOptional = userService.retrieve(courseOptional.get().getTeacherId());
            if (!userOptional.isPresent()) {
                return CourseUpdateResponse.newBuilder()
                        .setResponse(ControllerUtils.generateErrorBaseResponse(token, "teacher info not found"))
                        .build();
            }
            User teacher = userOptional.get();
            Course copyCourse = Course.copyFrom(courseOptional.get());
            if (request.getUpdateCourseName()) {
                copyCourse.setCourseName(request.getCourseName());
            }
            if (request.getUpdateDescription()) {
                copyCourse.setDescription(request.getDescription());
            }
            Map<String, byte[]> images = null;
            if (request.getUpdateImage()) {
                images = ControllerUtils.generateImageMap("course", copyCourse.getTeacherId(), request.getImageList());
            }
            if (request.getUpdateTags()) {
                copyCourse.getTags().clear();
                copyCourse.getTags().addAll(request.getTagList());
            }
            ServiceResult serviceResult = courseService.updateCourse(copyCourse, images);
            baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
            if (serviceResult.isSuccess()) {
                courseInfo = ProtoUtils.generateCourseInfo((Course) serviceResult.extra(Course.class), teacher, false);
            } else {
                courseInfo = ProtoUtils.generateCourseInfo(copyCourse, teacher, false);
            }
        }
        return CourseUpdateResponse.newBuilder()
                .setResponse(baseResponse)
                .setCourseInfo(courseInfo)
                .build();
    }

    @ResponseBody
    @RequestMapping(value = "/delete", produces = "application/x-protobuf", method = RequestMethod.POST)
    public CourseDeleteResponse deleteCourse(@RequestBody CourseDeleteRequest request) {
        String token = tokenManager.getOrNewToken(request.getRequest().getToken());
        BaseResponse baseResponse = ControllerUtils.validateTokenLogin(tokenManager, token);
        if (baseResponse != null) {
            return CourseDeleteResponse.newBuilder()
                    .setResponse(baseResponse)
                    .build();
        }
        ServiceResult serviceResult = courseService.deleteCourse(request.getCourseId());
        baseResponse = ControllerUtils.generateBaseResponseFrom(token, serviceResult);
        return CourseDeleteResponse.newBuilder()
                .setResponse(baseResponse)
                .setCourseId(request.getCourseId())
                .build();
    }
}
