package com.corkili.learningserver.repo;

import com.corkili.learningserver.po.TopicReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopicReplyRepository extends JpaRepository<TopicReply, Long> {

    @Query("select tr.id from TopicReply tr left join tr.belongComment trbc where trbc.id = ?1")
    List<Long> findAllTopicReplyIdByBelongCommentId(Long belongCommentId);

    void deleteAllByBelongCommentId(Long belongCommentId);

    List<TopicReply> findAllByBelongCommentId(Long belongCommentId);
}
