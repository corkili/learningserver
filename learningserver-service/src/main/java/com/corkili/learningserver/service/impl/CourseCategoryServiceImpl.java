package com.corkili.learningserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.CourseCategory;
import com.corkili.learningserver.repo.CourseCategoryRepository;
import com.corkili.learningserver.service.CourseCategoryService;

@Slf4j
@Service
public class CourseCategoryServiceImpl extends ServiceImpl<CourseCategory, com.corkili.learningserver.po.CourseCategory> implements CourseCategoryService {

    @Autowired
    private CourseCategoryRepository courseCategoryRepository;

    @Override
    protected JpaRepository<com.corkili.learningserver.po.CourseCategory, Long> repository() {
        return courseCategoryRepository;
    }

    @Override
    protected String entityName() {
        return "courseCategory";
    }

    @Override
    protected CourseCategory newBusinessObject() {
        return new CourseCategory();
    }

    @Override
    protected com.corkili.learningserver.po.CourseCategory newPersistObject() {
        return new com.corkili.learningserver.po.CourseCategory();
    }
}
