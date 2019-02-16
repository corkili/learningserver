package com.corkili.learningserver.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.po.Course;
import com.corkili.learningserver.po.User;

public interface CourseRepository extends JpaRepository<Course, Long> {

    List<Course> findAllByTeacher(User teacher);

    List<Course> findAllByTeacherIn(List<User> teachers);

    List<Course> findAllByTagsContainingOrCourseNameContaining(String keyword1, String keyword2);

}
