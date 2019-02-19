package com.corkili.learningserver.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.corkili.learningserver.po.CourseWork;
import com.corkili.learningserver.po.WorkQuestion;

public interface WorkQuestionRepository extends JpaRepository<WorkQuestion, Long> {

    List<WorkQuestion> findWorkQuestionsByBelongCourseWork(CourseWork courseWork);

    @Query("select wq.id from WorkQuestion wq left join wq.belongCourseWork wqbcw where wqbcw.id = ?1")
    List<Long> findAllWorkQuestionIdByBelongCourseWorkId(Long belongCourseWorkId);

    void deleteAllByBelongCourseWorkId(Long belongCourseWorkId);

    boolean existsWorkQuestionsByQuestionId(Long questionId);

}
