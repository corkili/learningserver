package com.corkili.learningserver.service;

import com.corkili.learningserver.bo.CourseWork;
import com.corkili.learningserver.common.ServiceResult;

public interface CourseWorkService extends Service<CourseWork, com.corkili.learningserver.po.CourseWork> {

    ServiceResult deleteCourseWork(Long courseWorkId);

    ServiceResult deleteCourseWorkByBelongCourseId(Long belongCourseId);

}
