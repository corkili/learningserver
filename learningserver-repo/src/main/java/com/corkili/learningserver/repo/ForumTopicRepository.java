package com.corkili.learningserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.po.ForumTopic;

public interface ForumTopicRepository extends JpaRepository<ForumTopic, Long> {

}
