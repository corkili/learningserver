package com.corkili.learningserver.service;

import java.util.List;
import java.util.Map;

import com.corkili.learningserver.bo.Course;
import com.corkili.learningserver.common.ServiceResult;

public interface CourseService extends Service<Course, com.corkili.learningserver.po.Course> {

    ServiceResult createCourse(Course course, Map<String, byte[]> images);

    ServiceResult findAllCourse(boolean all, Long teacherId, String teacherName, List<String> keywords);

    ServiceResult updateCourse(Course course, Map<String, byte[]> images);

    ServiceResult deleteCourse(Long courseId);

}
