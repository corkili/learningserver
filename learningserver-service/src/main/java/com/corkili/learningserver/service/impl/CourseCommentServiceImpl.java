package com.corkili.learningserver.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.CourseComment;
import com.corkili.learningserver.bo.CourseComment.Type;
import com.corkili.learningserver.repo.CourseCommentRepository;
import com.corkili.learningserver.service.CourseCommentService;

@Slf4j
@Service
public class CourseCommentServiceImpl extends ServiceImpl<CourseComment, com.corkili.learningserver.po.CourseComment> implements CourseCommentService {

    @Autowired
    private CourseCommentRepository courseCommentRepository;

    @Override
    public Optional<CourseComment> po2bo(com.corkili.learningserver.po.CourseComment courseCommentPO) {
        Optional<CourseComment> superOptional = super.po2bo(courseCommentPO);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        CourseComment courseComment = superOptional.get();
        courseComment.setCommentType(Type.valueOf(courseCommentPO.getCommentType().name()));
        courseComment.setImagePaths(courseCommentPO.getImagePaths());
        return Optional.of(courseComment);
    }

    @Override
    public Optional<com.corkili.learningserver.po.CourseComment> bo2po(CourseComment courseComment) {
        Optional<com.corkili.learningserver.po.CourseComment> superOptional = super.bo2po(courseComment);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        com.corkili.learningserver.po.CourseComment courseCommentPO = superOptional.get();
        courseCommentPO.setCommentType(com.corkili.learningserver.po.CourseComment.Type.valueOf(courseComment.getCommentType().name()));
        courseCommentPO.setImagePaths(courseComment.getImagePathsStr());
        return Optional.of(courseCommentPO);
    }

    @Override
    protected JpaRepository<com.corkili.learningserver.po.CourseComment, Long> repository() {
        return courseCommentRepository;
    }

    @Override
    protected String entityName() {
        return "courseComment";
    }

    @Override
    protected CourseComment newBusinessObject() {
        return new CourseComment();
    }

    @Override
    protected com.corkili.learningserver.po.CourseComment newPersistObject() {
        return new com.corkili.learningserver.po.CourseComment();
    }

    @Override
    protected Logger logger() {
        return log;
    }
}
