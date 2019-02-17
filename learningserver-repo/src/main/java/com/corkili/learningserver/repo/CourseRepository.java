package com.corkili.learningserver.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.po.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

//    @Query("select c from Course c left join c.teacher t where t.id = ?1")
    List<Course> findAllByTeacherId(Long teacherId);

    List<Course> findAllByTeacherUsernameContaining(String teacherUsername);

    List<Course> findAllByTagsContainingOrCourseNameContaining(String keyword1, String keyword2);

}
