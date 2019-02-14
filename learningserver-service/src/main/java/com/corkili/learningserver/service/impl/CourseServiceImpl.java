package com.corkili.learningserver.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.Course;
import com.corkili.learningserver.repo.CourseRepository;
import com.corkili.learningserver.service.CourseService;

@Slf4j
@Service
public class CourseServiceImpl extends ServiceImpl<Course, com.corkili.learningserver.po.Course> implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

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
}
