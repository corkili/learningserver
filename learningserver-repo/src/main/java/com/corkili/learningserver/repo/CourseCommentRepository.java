package com.corkili.learningserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.po.CourseComment;

public interface CourseCommentRepository extends JpaRepository<CourseComment, Long> {

}
