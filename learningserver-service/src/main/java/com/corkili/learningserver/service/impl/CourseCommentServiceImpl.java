package com.corkili.learningserver.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.CourseComment;
import com.corkili.learningserver.bo.CourseComment.Type;
import com.corkili.learningserver.common.ImageUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.repo.CourseCommentRepository;
import com.corkili.learningserver.repo.CourseRepository;
import com.corkili.learningserver.service.CourseCommentService;

@Slf4j
@Service
@Transactional
public class CourseCommentServiceImpl extends ServiceImpl<CourseComment, com.corkili.learningserver.po.CourseComment> implements CourseCommentService {

    @Autowired
    private CourseCommentRepository courseCommentRepository;

    @Autowired
    private CourseRepository courseRepository;

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

    @Override
    public ServiceResult deleteCourseComment(Long courseCommentId) {
        if (!delete(courseCommentId)) {
            return recordWarnAndCreateSuccessResultWithMessage("delete course comment success");
        }
        return ServiceResult.successResultWithMesage("delete course comment success");
    }

    @Override
    public ServiceResult deleteCourseCommentByCommentedCourseId(Long commentedCourseId) {
        List<Long> courseCommentIdList = courseCommentRepository.findAllCourseCommentIdByCommentedCourseId(commentedCourseId);
        courseCommentRepository.deleteAllByCommentedCourseId(commentedCourseId);
        for (Long id : courseCommentIdList) {
            evictFromCache(entityName() + id);
        }
        return ServiceResult.successResultWithMesage("delete course comment success");
    }

    @Override
    public ServiceResult createCourseComment(CourseComment courseComment, Map<String, byte[]> images) {
        if (courseComment.getCommentType() == null) {
            return recordErrorAndCreateFailResultWithMessage("create courseComment error: commentType is empty");
        }
        if (StringUtils.isBlank(courseComment.getContent())) {
            return recordErrorAndCreateFailResultWithMessage("create courseComment error: content is empty");
        }
        if (courseComment.getCommentAuthorId() == null) {
            return recordErrorAndCreateFailResultWithMessage("create courseComment error: authorId is null");
        }
        if (courseComment.getCommentedCourseId() == null || !courseRepository.existsById(courseComment.getCommentedCourseId())) {
            return recordErrorAndCreateFailResultWithMessage("create courseComment error: commentedCourseId is null or not exists");
        }
        if (!ImageUtils.storeImages(images)) {
            return recordErrorAndCreateFailResultWithMessage("create courseComment error: store image failed");
        }
        courseComment.getImagePaths().clear();
        courseComment.getImagePaths().addAll(images.keySet());
        Optional<CourseComment> courseCommentOptional = create(courseComment);
        if (!courseCommentOptional.isPresent()) {
            ImageUtils.deleteImages(images.keySet());
            return recordErrorAndCreateFailResultWithMessage("create courseComment error: create courseComment failed");
        }
        courseComment = courseCommentOptional.get();
        return ServiceResult.successResult("create courseComment success", CourseComment.class, courseComment);
    }

    @Override
    public ServiceResult updateCourseComment(CourseComment courseComment, Map<String, byte[]> images) {
        if (courseComment.getCommentType() == null) {
            return recordErrorAndCreateFailResultWithMessage("update courseComment error: commentType is empty");
        }
        if (StringUtils.isBlank(courseComment.getContent())) {
            return recordErrorAndCreateFailResultWithMessage("update courseComment error: content is empty");
        }
        if (courseComment.getCommentAuthorId() == null) {
            return recordErrorAndCreateFailResultWithMessage("update courseComment error: authorId is null");
        }
        if (courseComment.getCommentedCourseId() == null || !courseRepository.existsById(courseComment.getCommentedCourseId())) {
            return recordErrorAndCreateFailResultWithMessage("update courseComment error: commentedCourseId is null or not exists");
        }
        List<String> oldImagePaths = new LinkedList<>(courseComment.getImagePaths());
        if (images != null) {
            if (!ImageUtils.storeImages(images)) {
                return recordErrorAndCreateFailResultWithMessage("update courseComment error: store image failed");
            }
            courseComment.getImagePaths().clear();
            courseComment.getImagePaths().addAll(images.keySet());
        }
        Optional<CourseComment> courseCommentOptional = update(courseComment);
        if (!courseCommentOptional.isPresent()) {
            if (images != null) {
                ImageUtils.deleteImages(images.keySet());
            }
            courseComment.getImagePaths().clear();
            courseComment.getImagePaths().addAll(oldImagePaths);
            return recordErrorAndCreateFailResultWithMessage("update courseComment error: update courseComment failed");
        }
        if (images != null) {
            ImageUtils.deleteImages(oldImagePaths);
        }
        courseComment = courseCommentOptional.get();
        return ServiceResult.successResult("update courseComment success", CourseComment.class, courseComment);
    }

    @Override
    public ServiceResult findAllCourseComment(Long commentedCourseId) {
        if (commentedCourseId == null) {
            return recordWarnAndCreateSuccessResultWithMessage("find all courseComment warn: commentedCourseId is null")
                    .merge(ServiceResult.successResultWithExtra(List.class, new LinkedList<CourseComment>()), true);
        }
        List<com.corkili.learningserver.po.CourseComment> allCourseCommentPO =
                courseCommentRepository.findAllByCommentedCourseId(commentedCourseId);
        List<CourseComment> allCourseComment = new LinkedList<>();
        StringBuilder errId = new StringBuilder();
        int i = 0;
        for (com.corkili.learningserver.po.CourseComment courseCommentPO : allCourseCommentPO) {
            Optional<CourseComment> courseCommentOptional = po2bo(courseCommentPO);
            i++;
            if (!courseCommentOptional.isPresent()) {
                errId.append(courseCommentPO.getId());
                if (i != allCourseCommentPO.size()) {
                    errId.append(",");
                }
            } else {
                CourseComment courseComment = courseCommentOptional.get();
                allCourseComment.add(courseComment);
                // cache
                putToCache(entityName() + courseComment.getId(), courseComment);
            }
        }
        String msg = "find all courseComment success";
        if (errId.length() != 0) {
            msg = ServiceUtils.format("find all courseComment warn: transfer courseComment po [{}] to bo failed.", errId.toString());
            log.warn(msg);
        }
        return ServiceResult.successResult(msg, List.class, allCourseComment);
    }
}
