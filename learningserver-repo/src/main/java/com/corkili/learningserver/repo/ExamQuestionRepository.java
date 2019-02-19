package com.corkili.learningserver.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.corkili.learningserver.po.Exam;
import com.corkili.learningserver.po.ExamQuestion;

public interface ExamQuestionRepository extends JpaRepository<ExamQuestion, Long> {

    List<ExamQuestion> findExamQuestionsByBelongExam(Exam exam);

    @Query("select eq.id from ExamQuestion eq left join eq.belongExam eqbe where eqbe.id = ?1")
    List<Long> findAllExamQuestionIdByBelongExamId(Long belongExamId);

    void deleteAllByBelongExamId(Long belongExamId);

    boolean existsExamQuestionByQuestionId(Long questionId);

    boolean existsExamQuestionByBelongExamIdAndIndex(Long belongExamId, int index);

    Optional<ExamQuestion> findExamQuestionByBelongExamIdAndIndex(Long belongExamId, int index);

    @Query("select eq.id from ExamQuestion eq left join eq.belongExam e where e.id = ?1 and eq.index > ?2")
    List<Long> findAllExamQuestionIdByBelongExamIdAndIndexGreaterThan(Long belongExamId, int minIndex);

    void deleteAllByBelongExamIdAndIndexGreaterThan(Long belongExamId, int minIndex);

    List<ExamQuestion> findAllByBelongExamId(Long belongExamId);

}
