package com.corkili.learningserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.po.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
