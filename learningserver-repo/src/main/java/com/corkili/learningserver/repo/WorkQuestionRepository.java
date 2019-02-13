package com.corkili.learningserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.po.WorkQuestion;

public interface WorkQuestionRepository extends JpaRepository<WorkQuestion, Long> {

}
