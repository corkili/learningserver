package com.corkili.learningserver.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.corkili.learningserver.po.CourseWork;

public interface CourseWorkRepository extends JpaRepository<CourseWork, Long> {

    @Query("select cw.id from CourseWork cw left join cw.belongCourse cwbc where cwbc.id = ?1")
    List<Long> findAllCourseWorkIdByBelongCourseId(Long belongCourseId);

    void deleteAllByBelongCourseId(Long belongCourseId);

}
