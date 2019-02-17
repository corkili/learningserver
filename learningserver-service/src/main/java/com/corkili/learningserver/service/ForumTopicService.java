package com.corkili.learningserver.service;

import com.corkili.learningserver.bo.ForumTopic;
import com.corkili.learningserver.common.ServiceResult;

public interface ForumTopicService extends Service<ForumTopic, com.corkili.learningserver.po.ForumTopic> {

    ServiceResult deleteForumTopic(Long forumTopicId);

}
