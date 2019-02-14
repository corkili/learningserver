package com.corkili.learningserver.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.ForumTopic;
import com.corkili.learningserver.repo.ForumTopicRepository;
import com.corkili.learningserver.service.ForumTopicService;

@Slf4j
@Service
public class ForumTopicServiceImpl extends ServiceImpl<ForumTopic, com.corkili.learningserver.po.ForumTopic> implements ForumTopicService {

    @Autowired
    private ForumTopicRepository forumTopicRepository;

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
}
