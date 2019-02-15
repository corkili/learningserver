package com.corkili.learningserver.service;

import java.util.Map;

import com.corkili.learningserver.bo.Course;
import com.corkili.learningserver.common.ServiceResult;

public interface CourseService extends Service<Course, com.corkili.learningserver.po.Course> {

    ServiceResult createCourse(Course course, Map<String, byte[]> images);

}
