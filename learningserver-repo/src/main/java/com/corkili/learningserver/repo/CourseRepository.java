package com.corkili.learningserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.po.Course;

public interface CourseRepository extends JpaRepository<Course, Long> {

}
