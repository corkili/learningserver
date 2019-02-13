package com.corkili.learningserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.po.Exam;

public interface ExamRepository extends JpaRepository<Exam, Long> {

}
