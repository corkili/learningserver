package com.corkili.learningserver.repo;

import com.corkili.learningserver.po.TopicComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopicCommentRepository extends JpaRepository<TopicComment, Long> {

    @Query("select tc.id from TopicComment tc left join tc.belongTopic tcbt where tcbt.id = ?1")
    List<Long> findAllTopicCommentIdByBelongTopicId(Long belongTopicId);

    void deleteAllByBelongTopicId(Long belongTopicId);

    List<TopicComment> findAllByBelongTopicId(Long belongTopicId);
}
