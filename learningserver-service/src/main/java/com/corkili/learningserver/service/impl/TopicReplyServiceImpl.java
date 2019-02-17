package com.corkili.learningserver.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.TopicReply;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.repo.TopicReplyRepository;
import com.corkili.learningserver.service.TopicReplyService;

@Slf4j
@Service
public class TopicReplyServiceImpl extends ServiceImpl<TopicReply, com.corkili.learningserver.po.TopicReply> implements TopicReplyService {

    @Autowired
    private TopicReplyRepository topicReplyRepository;

    @Override
    public Optional<TopicReply> po2bo(com.corkili.learningserver.po.TopicReply topicReplyPO) {
        Optional<TopicReply> superOptional = super.po2bo(topicReplyPO);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        TopicReply topicReply = superOptional.get();
        topicReply.setImagePaths(topicReplyPO.getImagePaths());
        return Optional.of(topicReply);
    }

    @Override
    public Optional<com.corkili.learningserver.po.TopicReply> bo2po(TopicReply topicReply) {
        Optional<com.corkili.learningserver.po.TopicReply> superOptional = super.bo2po(topicReply);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        com.corkili.learningserver.po.TopicReply topicReplyPO = superOptional.get();
        topicReplyPO.setImagePaths(topicReply.getImagePathsStr());
        return Optional.of(topicReplyPO);
    }

    @Override
    protected JpaRepository<com.corkili.learningserver.po.TopicReply, Long> repository() {
        return topicReplyRepository;
    }

    @Override
    protected String entityName() {
        return "topicReply";
    }

    @Override
    protected TopicReply newBusinessObject() {
        return new TopicReply();
    }

    @Override
    protected com.corkili.learningserver.po.TopicReply newPersistObject() {
        return new com.corkili.learningserver.po.TopicReply();
    }

    @Override
    protected Logger logger() {
        return log;
    }

    @Override
    public ServiceResult deleteTopicReply(Long topicReplyId) {
        if (!delete(topicReplyId)) {
            return recordWarnAndCreateSuccessResultWithMessage("delete topic reply success");
        }
        return ServiceResult.successResultWithMesage("delete topic reply success");
    }
}
