package com.corkili.learningserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.po.SubmittedExam;

public interface SubmittedExamRepository extends JpaRepository<SubmittedExam, Long> {

}
