package com.corkili.learningserver.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.corkili.learningserver.po.Exam;
import com.corkili.learningserver.po.ExamQuestion;

public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {

    List<ExamQuestion> findExamQuestionsByBelongExam(Exam exam);

    @Query("select eq.id from ExamQuestion eq left join eq.belongExam eqbe where eqbe.id = ?1")
    List<Long> findAllExamQuestionIdByBelongExamId(Long belongExamId);

}
