package com.corkili.learningserver.repo;

import com.corkili.learningserver.po.SubmittedCourseWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubmittedCourseWorkRepository extends JpaRepository<SubmittedCourseWork, Long> {

    @Query("select scw.id from SubmittedCourseWork scw left join scw.belongCourseWork scwbcw where scwbcw.id = ?1")
    List<Long> findAllSubmittedCourseWorkIdByBelongCourseWorkId(Long belongCourseWorkId);

    void deleteAllByBelongCourseWorkId(Long belongCourseWorkId);

    boolean existsByBelongCourseWorkIdAndSubmitterId(Long belongCourseWorkId, Long submitterId);

    Optional<SubmittedCourseWork> findByBelongCourseWorkIdAndSubmitterId(Long belongCourseWorkId, Long submitterId);

    List<SubmittedCourseWork> findAllByBelongCourseWorkId(Long belongCourseWorkId);

}
