package com.corkili.learningserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.CourseWork;
import com.corkili.learningserver.repo.CourseWorkRepository;
import com.corkili.learningserver.service.CourseWorkService;

@Slf4j
@Service
public class CourseWorkServiceImpl extends ServiceImpl<CourseWork, com.corkili.learningserver.po.CourseWork> implements CourseWorkService {

    @Autowired
    private CourseWorkRepository courseWorkRepository;

    @Override
    protected JpaRepository<com.corkili.learningserver.po.CourseWork, Long> repository() {
        return courseWorkRepository;
    }

    @Override
    protected String entityName() {
        return "courseWork";
    }

    @Override
    protected CourseWork newBusinessObject() {
        return new CourseWork();
    }

    @Override
    protected com.corkili.learningserver.po.CourseWork newPersistObject() {
        return new com.corkili.learningserver.po.CourseWork();
    }
}
