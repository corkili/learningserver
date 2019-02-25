package com.corkili.learningserver.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.ForumTopic;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.repo.ForumTopicRepository;
import com.corkili.learningserver.service.ForumTopicService;
import com.corkili.learningserver.service.TopicCommentService;

@Slf4j
@Service
public class ForumTopicServiceImpl extends ServiceImpl<ForumTopic, com.corkili.learningserver.po.ForumTopic> implements ForumTopicService {

    @Autowired
    private ForumTopicRepository forumTopicRepository;

    @Autowired
    private TopicCommentService topicCommentService;

    @Override
    public Optional<ForumTopic> po2bo(com.corkili.learningserver.po.ForumTopic forumTopicPO) {
        Optional<ForumTopic> superOptional = super.po2bo(forumTopicPO);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        ForumTopic forumTopic = superOptional.get();
        forumTopic.setImagePaths(forumTopicPO.getImagePaths());
        return Optional.of(forumTopic);
    }

    @Override
    public Optional<com.corkili.learningserver.po.ForumTopic> bo2po(ForumTopic forumTopic) {
        Optional<com.corkili.learningserver.po.ForumTopic> superOptional = super.bo2po(forumTopic);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        com.corkili.learningserver.po.ForumTopic forumTopicPO = superOptional.get();
        forumTopicPO.setImagePaths(forumTopic.getImagePathsStr());
        return Optional.of(forumTopicPO);
    }

    @Override
    protected JpaRepository<com.corkili.learningserver.po.ForumTopic, Long> repository() {
        return forumTopicRepository;
    }

    @Override
    protected String entityName() {
        return "forumTopic";
    }

    @Override
    protected ForumTopic newBusinessObject() {
        return new ForumTopic();
    }

    @Override
    protected com.corkili.learningserver.po.ForumTopic newPersistObject() {
        return new com.corkili.learningserver.po.ForumTopic();
    }

    @Override
    protected Logger logger() {
        return log;
    }

    @Override
    public ServiceResult deleteForumTopic(Long forumTopicId) {
        ServiceResult serviceResult;
        if (delete(forumTopicId)) {
            serviceResult = ServiceResult.successResultWithMesage("delete forum topic success");
        } else {
            serviceResult = recordWarnAndCreateSuccessResultWithMessage("delete forum topic success");
        }
        // delete associated topic comment
        serviceResult = serviceResult.mergeFrom(topicCommentService.deleteTopicCommentByBelongTopicId(forumTopicId), true);
        return serviceResult;
    }

    @Override
    public ServiceResult deleteForumTopicByBelongCourseId(Long belongCourseId) {
        List<Long> forumTopicIdList = forumTopicRepository.findAllForumTopicIdByBelongCourseId(belongCourseId);
        forumTopicRepository.deleteAllByBelongCourseId(belongCourseId);
        for (Long id : forumTopicIdList) {
            topicCommentService.deleteTopicCommentByBelongTopicId(id);
            evictFromCache(entityName() + id);
        }
        return ServiceResult.successResultWithMesage("delete forum topic success");
    }
}
