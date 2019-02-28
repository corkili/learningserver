package com.corkili.learningserver.service;

import com.corkili.learningserver.bo.CourseComment;
import com.corkili.learningserver.common.ServiceResult;

import java.util.Map;

public interface CourseCommentService extends Service<CourseComment, com.corkili.learningserver.po.CourseComment> {

    ServiceResult deleteCourseComment(Long courseCommentId);

    ServiceResult deleteCourseCommentByCommentedCourseId(Long commentedCourseId);

    ServiceResult createCourseComment(CourseComment courseComment, Map<String, byte[]> images);

    ServiceResult updateCourseComment(CourseComment courseComment, Map<String, byte[]> images);

    ServiceResult findAllCourseComment(Long commentedCourseId);

}
