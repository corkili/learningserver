package com.corkili.learningserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.po.SubmittedCourseWork;

public interface SubmittedCourseWorkRepository extends JpaRepository<SubmittedCourseWork, Long> {

}
