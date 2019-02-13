package com.corkili.learningserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.po.CourseCategory;

public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Long> {

}
