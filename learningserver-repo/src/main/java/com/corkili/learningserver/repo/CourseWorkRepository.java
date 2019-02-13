package com.corkili.learningserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.po.CourseWork;

public interface CourseWorkRepository extends JpaRepository<CourseWork, Long> {

}
