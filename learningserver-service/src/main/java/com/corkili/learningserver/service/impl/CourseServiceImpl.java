package com.corkili.learningserver.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.Course;
import com.corkili.learningserver.common.ImageUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.repo.CourseRepository;
import com.corkili.learningserver.service.CourseCommentService;
import com.corkili.learningserver.service.CourseService;
import com.corkili.learningserver.service.CourseSubscriptionService;
import com.corkili.learningserver.service.CourseWorkService;
import com.corkili.learningserver.service.ExamService;
import com.corkili.learningserver.service.ForumTopicService;
import com.corkili.learningserver.service.ScormService;

@Slf4j
@Service
public class CourseServiceImpl extends ServiceImpl<Course, com.corkili.learningserver.po.Course> implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ScormService scormService;

    @Autowired
    private CourseCommentService courseCommentService;

    @Autowired
    private CourseSubscriptionService courseSubscriptionService;

    @Autowired
    private ForumTopicService forumTopicService;

    @Autowired
    private CourseWorkService courseWorkService;

    @Autowired
    private ExamService examService;

    @Override
    public Optional<Course> po2bo(com.corkili.learningserver.po.Course coursePO) {
        Optional<Course> superOptional = super.po2bo(coursePO);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        String id = coursePO.getId() == null ? "" : coursePO.getId().toString();
        Course course = superOptional.get();
        course.setImagePaths(coursePO.getImagePaths());
        if (course.setTags(coursePO.getTags())) {
            log.error("tags of course po [{}] is empty", id);
            return Optional.empty();
        }
        return Optional.of(course);
    }

    @Override
    public Optional<com.corkili.learningserver.po.Course> bo2po(Course course) {
        Optional<com.corkili.learningserver.po.Course> superOptional = super.bo2po(course);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        com.corkili.learningserver.po.Course coursePO = superOptional.get();
        coursePO.setImagePaths(course.getImagePathsStr());
        coursePO.setTags(course.getTagsStr());
        return Optional.of(coursePO);
    }

    @Override
    protected JpaRepository<com.corkili.learningserver.po.Course, Long> repository() {
        return courseRepository;
    }

    @Override
    protected String entityName() {
        return "course";
    }

    @Override
    protected Course newBusinessObject() {
        return new Course();
    }

    @Override
    protected com.corkili.learningserver.po.Course newPersistObject() {
        return new com.corkili.learningserver.po.Course();
    }

    @Override
    protected Logger logger() {
        return log;
    }

    @Override
    public ServiceResult createCourse(Course course, Map<String, byte[]> images) {
        if (StringUtils.isBlank(course.getCourseName())) {
            return recordErrorAndCreateFailResultWithMessage("create course error: courseName is empty");
        }
        if (StringUtils.isBlank(course.getDescription())) {
            return recordErrorAndCreateFailResultWithMessage("create course error: description is empty");
        }
        if (course.getTags().isEmpty()) {
            return recordErrorAndCreateFailResultWithMessage("create course error: tag is empty");
        }
        if (course.getTeacherId() == null) {
            return recordErrorAndCreateFailResultWithMessage("create course error: teacher is null");
        }
        if (ImageUtils.storeImages(images)) {
            return recordErrorAndCreateFailResultWithMessage("create course error: store image failed");
        }
        course.getImagePaths().clear();
        course.getImagePaths().addAll(images.keySet());
        Optional<Course> courseOptional = create(course);
        if (!courseOptional.isPresent()) {
            ImageUtils.deleteImages(images.keySet());
            return recordErrorAndCreateFailResultWithMessage("create course error: create course failed");
        }
        course = courseOptional.get();
        return ServiceResult.successResult("create course success", Course.class, course);
    }

    @Override
    public ServiceResult findAllCourse(boolean all, Long teacherId, String teacherName, List<String> keywords) {
        String msg = "find all course success";
        Map<Long, com.corkili.learningserver.po.Course> coursePOMap = new HashMap<>();
        if (all) {
            List<com.corkili.learningserver.po.Course> allCoursePO = courseRepository.findAll();
            for (com.corkili.learningserver.po.Course course : allCoursePO) {
                coursePOMap.put(course.getId(), course);
            }
        } else {
            if (teacherId != null) {
                List<com.corkili.learningserver.po.Course> coursePOList = courseRepository.findAllByTeacherId(teacherId);
                for (com.corkili.learningserver.po.Course course : coursePOList) {
                    coursePOMap.put(course.getId(), course);
                }
            }
            if (StringUtils.isNotBlank(teacherName)) {
                List<com.corkili.learningserver.po.Course> coursePOList = courseRepository.findAllByTeacherUsernameContaining(teacherName);
                for (com.corkili.learningserver.po.Course course : coursePOList) {
                    coursePOMap.put(course.getId(), course);
                }
            }
            if (keywords != null && !keywords.isEmpty()) {
                for (String keyword : keywords) {
                    List<com.corkili.learningserver.po.Course> coursePOList = courseRepository
                            .findAllByTagsContainingOrCourseNameContaining(keyword, keyword);
                    for (com.corkili.learningserver.po.Course course : coursePOList) {
                        coursePOMap.put(course.getId(), course);
                    }
                }
            }
        }
        List<Course> allCourse = new LinkedList<>();
        Collection<com.corkili.learningserver.po.Course> allCoursePO = coursePOMap.values();
        StringBuilder errId = new StringBuilder();
        int i = 0;
        for (com.corkili.learningserver.po.Course coursePO : allCoursePO) {
            Optional<Course> courseOptional = po2bo(coursePO);
            i++;
            if (!courseOptional.isPresent()) {
                errId.append(coursePO.getId());
                if (i != allCoursePO.size()) {
                    errId.append(",");
                }
            } else {
                Course course = courseOptional.get();
                allCourse.add(course);
                // cache
                putToCache(entityName() + course.getId(), course);
            }
        }
        if (errId.length() != 0) {
            msg = ServiceUtils.format("find all course warn: transfer course po [{}] to bo failed.", errId.toString());
            log.warn(msg);
        }
        return ServiceResult.successResult(msg, List.class, allCourse);
    }

    @Override
    public ServiceResult updateCourse(Course course, Map<String, byte[]> images) {
        if (StringUtils.isBlank(course.getCourseName())) {
            return recordErrorAndCreateFailResultWithMessage("update course error: courseName is empty");
        }
        if (StringUtils.isBlank(course.getDescription())) {
            return recordErrorAndCreateFailResultWithMessage("update course error: description is empty");
        }
        if (course.getTags().isEmpty()) {
            return recordErrorAndCreateFailResultWithMessage("update course error: tag is empty");
        }
        if (course.getTeacherId() == null) {
            return recordErrorAndCreateFailResultWithMessage("update course error: teacher is null");
        }
        List<String> oldImagePaths = new LinkedList<>(course.getImagePaths());
        if (images != null) {
            if (!ImageUtils.storeImages(images)) {
                return recordErrorAndCreateFailResultWithMessage("update course error: store image failed");
            }
            course.getImagePaths().clear();
            course.getImagePaths().addAll(images.keySet());
        }
        Optional<Course> courseOptional = update(course);
        if (!courseOptional.isPresent()) {
            if (images != null) {
                ImageUtils.deleteImages(images.keySet());
            }
            course.getImagePaths().clear();
            course.getImagePaths().addAll(oldImagePaths);
            return recordErrorAndCreateFailResultWithMessage("update course error: create course failed");
        }
        if (images != null) {
            ImageUtils.deleteImages(oldImagePaths);
        }
        return ServiceResult.successResult("update course success", Course.class, courseOptional.get());
    }

    @Override
    public ServiceResult deleteCourse(Long courseId) {
        // delete associated scorm
        Optional<com.corkili.learningserver.po.Course> coursePOOptional = courseRepository.findById(courseId);
        if (coursePOOptional.isPresent()) {
            com.corkili.learningserver.po.Course coursePO = coursePOOptional.get();
            if (!ServiceUtils.isEntityReferenceNull(coursePO.getCourseware())) {
                scormService.deleteScorm(coursePO.getCourseware().getId());
            }
        }
        ServiceResult serviceResult;
        // delete course
        if (delete(courseId)) {
            serviceResult = ServiceResult.successResultWithMesage("delete course success");
        } else {
            serviceResult = recordWarnAndCreateSuccessResultWithMessage("delete course success");
        }
        // delete associated course comment
        serviceResult = serviceResult.mergeFrom(courseCommentService.deleteCourseCommentByCommentedCourseId(courseId), true);
        // delete associated course subscription
        serviceResult = serviceResult.mergeFrom(courseSubscriptionService.deleteCourseSubscriptionBySubscribedCourseId(courseId), true);
        // delete associated forum topic
        serviceResult = serviceResult.mergeFrom(forumTopicService.deleteForumTopicByBelongCourseId(courseId), true);
        // delete associated course work
        serviceResult = serviceResult.mergeFrom(courseWorkService.deleteCourseWorkByBelongCourseId(courseId), true);
        // delete associated exam
        serviceResult = serviceResult.mergeFrom(examService.deleteExamByBelongCourseId(courseId), true);
        return serviceResult;
    }
}
