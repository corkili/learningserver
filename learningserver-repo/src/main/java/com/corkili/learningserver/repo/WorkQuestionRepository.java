package com.corkili.learningserver.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.corkili.learningserver.po.CourseWork;
import com.corkili.learningserver.po.WorkQuestion;

public interface WorkQuestionRepository extends JpaRepository<WorkQuestion, Long> {

    List<WorkQuestion> findWorkQuestionsByBelongCourseWork(CourseWork courseWork);

    @Query("select wq.id from WorkQuestion wq left join wq.belongCourseWork wqbcw where wqbcw.id = ?1")
    List<Long> findAllWorkQuestionIdByBelongCourseWorkId(Long belongCourseWorkId);

    void deleteAllByBelongCourseWorkId(Long belongCourseWorkId);

    boolean existsWorkQuestionByQuestionId(Long questionId);

    boolean existsWorkQuestionByBelongCourseWorkIdAndIndex(Long belongCourseWorkId, int index);

    Optional<WorkQuestion> findWorkQuestionByBelongCourseWorkIdAndIndex(Long belongCourseWorkId, int index);

    @Query("select wq.id from WorkQuestion wq left join wq.belongCourseWork c where c.id = ?1 and wq.index > ?2")
    List<Long> findAllWorkQuestionIdByBelongCourseWorkIdAndIndexGreaterThan(Long belongCourseWorkId, int minIndex);

    void deleteAllByBelongCourseWorkIdAndIndexGreaterThan(Long belongCourseWorkId, int minIndex);

}
