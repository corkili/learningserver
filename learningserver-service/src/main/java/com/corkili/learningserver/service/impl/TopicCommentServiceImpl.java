package com.corkili.learningserver.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.TopicComment;
import com.corkili.learningserver.repo.TopicCommentRepository;
import com.corkili.learningserver.service.TopicCommentService;

@Slf4j
@Service
public class TopicCommentServiceImpl extends ServiceImpl<TopicComment, com.corkili.learningserver.po.TopicComment> implements TopicCommentService {

    @Autowired
    private TopicCommentRepository topicCommentRepository;

    @Override
    public Optional<TopicComment> po2bo(com.corkili.learningserver.po.TopicComment topicCommentPO) {
        Optional<TopicComment> superOptional = super.po2bo(topicCommentPO);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        TopicComment topicComment = superOptional.get();
        topicComment.setImagePaths(topicCommentPO.getImagePaths());
        return Optional.of(topicComment);
    }

    @Override
    public Optional<com.corkili.learningserver.po.TopicComment> bo2po(TopicComment topicComment) {
        Optional<com.corkili.learningserver.po.TopicComment> superOptional = super.bo2po(topicComment);
        if (!superOptional.isPresent()) {
            return Optional.empty();
        }
        com.corkili.learningserver.po.TopicComment topicCommentPO = superOptional.get();
        topicCommentPO.setImagePaths(topicComment.getImagePathsStr());
        return Optional.of(topicCommentPO);
    }

    @Override
    protected JpaRepository<com.corkili.learningserver.po.TopicComment, Long> repository() {
        return topicCommentRepository;
    }

    @Override
    protected String entityName() {
        return "topicComment";
    }

    @Override
    protected TopicComment newBusinessObject() {
        return new TopicComment();
    }

    @Override
    protected com.corkili.learningserver.po.TopicComment newPersistObject() {
        return new com.corkili.learningserver.po.TopicComment();
    }

    @Override
    protected Logger logger() {
        return log;
    }
}
