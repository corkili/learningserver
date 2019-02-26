package com.corkili.learningserver.repo;

import com.corkili.learningserver.po.SubmittedExam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubmittedExamRepository extends JpaRepository<SubmittedExam, Long> {

    @Query("select se.id from SubmittedExam se left join se.belongExam sebe where sebe.id = ?1")
    List<Long> findAllSubmittedExamIdByBelongExamId(Long belongExamId);

    void deleteAllByBelongExamId(Long belongExamId);

    boolean existsByBelongExamIdAndSubmitterId(Long belongExamId, Long submitterId);

    Optional<SubmittedExam> findByBelongExamIdAndSubmitterId(Long belongExamId, Long submitterId);

    List<SubmittedExam> findAllByBelongExamId(Long belongExamId);

}
