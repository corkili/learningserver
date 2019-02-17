package com.corkili.learningserver.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.corkili.learningserver.po.Exam;

public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query("select e.id from Exam e left join e.belongCourse ebc where ebc.id = ?1")
    List<Long> findAllExamIdByBelongCourseId(Long belongCourseId);

}
