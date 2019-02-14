package com.corkili.learningserver.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.po.Exam;
import com.corkili.learningserver.po.ExamQuestion;

public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {

    List<ExamQuestion> findExamQuestionsByBelongExam(Exam exam);

}
