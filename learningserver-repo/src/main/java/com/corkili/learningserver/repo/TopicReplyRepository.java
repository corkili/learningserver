package com.corkili.learningserver.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.corkili.learningserver.po.TopicReply;

public interface TopicReplyRepository extends JpaRepository<TopicReply, Long> {

    @Query("select tr.id from TopicReply tr left join tr.belongComment trbc where trbc.id = ?1")
    List<Long> findAllTopicReplyIdByBelongCommentId(Long belongCommentId);

    void deleteAllByBelongCommentId(Long belongCommentId);

}
