package com.corkili.learningserver.service;

import com.corkili.learningserver.bo.TopicReply;
import com.corkili.learningserver.common.ServiceResult;

public interface TopicReplyService extends Service<TopicReply, com.corkili.learningserver.po.TopicReply> {

    ServiceResult deleteTopicReply(Long topicReplyId);

    ServiceResult deleteTopicReplyByBelongCommentId(Long belongCommentId);

}
