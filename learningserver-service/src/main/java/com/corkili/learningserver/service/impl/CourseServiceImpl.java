package com.corkili.learningserver.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.Course;
import com.corkili.learningserver.common.ImageUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.po.User;
import com.corkili.learningserver.repo.CourseRepository;
import com.corkili.learningserver.repo.UserRepository;
import com.corkili.learningserver.service.CourseService;

@Slf4j
@Service
public class CourseServiceImpl extends ServiceImpl<Course, com.corkili.learningserver.po.Course> implements CourseService {

    private static final String CACHE_NAME = "memoryCache";

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CacheManager cacheManager;

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
        boolean storeSuccess = true;
        for (Entry<String, byte[]> entry : images.entrySet()) {
            if (!ImageUtils.storeImage(entry.getKey(), entry.getValue())) {
                storeSuccess = false;
            }
        }
        if (!storeSuccess) {
            for (String path : images.keySet()) {
                ImageUtils.deleteImage(path);
            }
            return recordErrorAndCreateFailResultWithMessage("create course error: store image failed");
        }
        Optional<Course> courseOptional = create(course);
        if (!courseOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage("create course error: create course failed");
        }
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
                Optional<com.corkili.learningserver.po.User> userPOOptional = userRepository.findById(teacherId);
                if (!userPOOptional.isPresent()) {
                    msg = "find all course warn: retrieve teacher failed";
                } else {
                    List<com.corkili.learningserver.po.Course> coursePOList = courseRepository
                            .findAllByTeacher(userPOOptional.get());
                    for (com.corkili.learningserver.po.Course course : coursePOList) {
                        coursePOMap.put(course.getId(), course);
                    }
                }
            }
            if (StringUtils.isNotBlank(teacherName)) {
                List<com.corkili.learningserver.po.User> teacherList = userRepository.findAllByUsernameContaining(teacherName);
                for (User teacher : teacherList) {
                    List<com.corkili.learningserver.po.Course> coursePOList = courseRepository.findAllByTeacher(teacher);
                    for (com.corkili.learningserver.po.Course course : coursePOList) {
                        coursePOMap.put(course.getId(), course);
                    }
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
        Cache cache = cacheManager.getCache(CACHE_NAME);
        int i = 0;
        for (com.corkili.learningserver.po.Course coursePO : allCoursePO) {
            Optional<Course> courseOptional = po2bo(coursePO);
            if (!courseOptional.isPresent()) {
                errId.append(coursePO.getId());
                if (++i != allCoursePO.size()) {
                    errId.append(",");
                }
            } else {
                Course course = courseOptional.get();
                allCourse.add(course);
                // cache
                if (cache != null) {
                    cache.put(entityName() + course.getId(), course);
                }
            }
        }
        if (errId.length() != 0) {
            msg = ServiceUtils.format("find all course warn: transfer course po [{}] to bo failed.", errId.toString());
        }
        return ServiceResult.successResult(msg, List.class, allCourse);
    }
}
