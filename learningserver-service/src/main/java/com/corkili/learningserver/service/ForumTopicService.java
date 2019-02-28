package com.corkili.learningserver.service;

import com.corkili.learningserver.bo.ForumTopic;
import com.corkili.learningserver.common.ServiceResult;

import java.util.Map;

public interface ForumTopicService extends Service<ForumTopic, com.corkili.learningserver.po.ForumTopic> {

    ServiceResult deleteForumTopic(Long forumTopicId);

    ServiceResult deleteForumTopicByBelongCourseId(Long belongCourseId);

    ServiceResult createForumTopic(ForumTopic forumTopic, Map<String, byte[]> images);

    ServiceResult updateForumTopic(ForumTopic forumTopic, Map<String, byte[]> images);

    ServiceResult findAllForumTopic(Long belongCourseId);

}
