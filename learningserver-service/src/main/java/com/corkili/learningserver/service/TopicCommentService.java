package com.corkili.learningserver.service;

import com.corkili.learningserver.bo.TopicComment;
import com.corkili.learningserver.common.ServiceResult;

public interface TopicCommentService extends Service<TopicComment, com.corkili.learningserver.po.TopicComment> {

    ServiceResult deleteTopicComment(Long topicCommentId);

    ServiceResult deleteTopicCommentByBelongTopicId(Long belongTopicId);

}
