package com.corkili.learningserver.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.corkili.learningserver.po.TopicComment;

public interface TopicCommentRepository extends JpaRepository<TopicComment, Long> {

    @Query("select tc.id from TopicComment tc left join tc.belongTopic tcbt where tcbt.id = ?1")
    List<Long> findAllTopicCommentIdByBelongTopicId(Long belongTopicId);

}
