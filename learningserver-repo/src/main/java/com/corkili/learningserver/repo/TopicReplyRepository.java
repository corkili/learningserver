package com.corkili.learningserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.po.TopicReply;

public interface TopicReplyRepository extends JpaRepository<TopicReply, Long> {

}
