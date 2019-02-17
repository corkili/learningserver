package com.corkili.learningserver.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.corkili.learningserver.po.CourseComment;

public interface CourseCommentRepository extends JpaRepository<CourseComment, Long> {

    @Query("select cc.id from CourseComment cc left join cc.commentedCourse c where c.id = ?1")
    List<Long> findAllCourseCommentIdByCommentedCourseId(Long commentedCourseId);

    void deleteAllByCommentedCourseId(Long commentedCourseId);

}
