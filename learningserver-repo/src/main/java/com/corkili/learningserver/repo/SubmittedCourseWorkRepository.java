package com.corkili.learningserver.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.corkili.learningserver.po.SubmittedCourseWork;

public interface SubmittedCourseWorkRepository extends JpaRepository<SubmittedCourseWork, Long> {

    @Query("select scw.id from SubmittedCourseWork scw left join scw.belongCourseWork scwbcw where scwbcw.id = ?1")
    List<Long> findAllSubmittedCourseWorkIdByBelongCourseWorkId(Long belongCourseWorkId);

    void deleteAllByBelongCourseWorkId(Long belongCourseWorkId);

}
