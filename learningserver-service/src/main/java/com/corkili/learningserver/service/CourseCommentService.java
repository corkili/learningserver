package com.corkili.learningserver.service;

import com.corkili.learningserver.bo.CourseComment;
import com.corkili.learningserver.common.ServiceResult;

public interface CourseCommentService extends Service<CourseComment, com.corkili.learningserver.po.CourseComment> {

    ServiceResult deleteCourseComment(Long courseCommentId);

}
