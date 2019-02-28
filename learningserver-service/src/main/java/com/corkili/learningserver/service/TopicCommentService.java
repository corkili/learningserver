package com.corkili.learningserver.service;

import com.corkili.learningserver.bo.TopicComment;
import com.corkili.learningserver.common.ServiceResult;

import java.util.Map;

public interface TopicCommentService extends Service<TopicComment, com.corkili.learningserver.po.TopicComment> {

    ServiceResult deleteTopicComment(Long topicCommentId);

    ServiceResult deleteTopicCommentByBelongTopicId(Long belongTopicId);

    ServiceResult createTopicComment(TopicComment topicComment, Map<String, byte[]> images);

    ServiceResult updateTopicComment(TopicComment topicComment, Map<String, byte[]> images);

    ServiceResult findAllTopicComment(Long belongTopicId);

}
