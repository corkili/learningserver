package com.corkili.learningserver.repo;

import com.corkili.learningserver.po.CourseComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseCommentRepository extends JpaRepository<CourseComment, Long> {

    @Query("select cc.id from CourseComment cc left join cc.commentedCourse c where c.id = ?1")
    List<Long> findAllCourseCommentIdByCommentedCourseId(Long commentedCourseId);

    void deleteAllByCommentedCourseId(Long commentedCourseId);

    List<CourseComment> findAllByCommentedCourseId(Long commentedCourseId);
}
