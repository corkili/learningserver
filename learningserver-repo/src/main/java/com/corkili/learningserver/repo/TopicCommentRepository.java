package com.corkili.learningserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.po.TopicComment;

public interface TopicCommentRepository extends JpaRepository<TopicComment, Long> {

}
