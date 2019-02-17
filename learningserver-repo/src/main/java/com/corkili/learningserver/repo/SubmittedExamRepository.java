package com.corkili.learningserver.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.corkili.learningserver.po.SubmittedExam;

public interface SubmittedExamRepository extends JpaRepository<SubmittedExam, Long> {

    @Query("select se.id from SubmittedExam se left join se.belongExam sebe where sebe.id = ?1")
    List<Long> findAllSubmittedExamIdByBelongExam(Long submittedExamId);

}
