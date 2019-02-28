package com.corkili.learningserver.service;

import com.corkili.learningserver.bo.TopicReply;
import com.corkili.learningserver.common.ServiceResult;

import java.util.Map;

public interface TopicReplyService extends Service<TopicReply, com.corkili.learningserver.po.TopicReply> {

    ServiceResult deleteTopicReply(Long topicReplyId);

    ServiceResult deleteTopicReplyByBelongCommentId(Long belongCommentId);

    ServiceResult createTopicReply(TopicReply topicReply, Map<String, byte[]> images);

    ServiceResult updateTopicReply(TopicReply topicReply, Map<String, byte[]> images);

    ServiceResult findAllTopicReply(Long belongCommentId);

}
