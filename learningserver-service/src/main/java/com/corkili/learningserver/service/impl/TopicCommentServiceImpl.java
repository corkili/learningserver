package com.corkili.learningserver.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.TopicComment;
import com.corkili.learningserver.common.ImageUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.repo.ForumTopicRepository;
import com.corkili.learningserver.repo.TopicCommentRepository;
import com.corkili.learningserver.service.TopicCommentService;
import com.corkili.learningserver.service.TopicReplyService;

@Slf4j
@Service
@Transactional
public class TopicCommentServiceImpl extends ServiceImpl<TopicComment, com.corkili.learningserver.po.TopicComment> implements TopicCommentService {

    @Autowired
    private TopicCommentRepository topicCommentRepository;

    @Autowired
    private TopicReplyService topicReplyService;
    
    @Autowired
    private ForumTopicRepository forumTopicRepository;

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

    @Override
    public ServiceResult deleteTopicComment(Long topicCommentId) {
        // delete associated topic reply
        topicReplyService.deleteTopicReplyByBelongCommentId(topicCommentId);
        ServiceResult serviceResult;
        if (delete(topicCommentId)) {
            serviceResult = ServiceResult.successResult("delete topic comment success");
        } else {
            serviceResult = recordWarnAndCreateSuccessResultWithMessage("delete topic comment success");
        }
        return serviceResult;
    }

    @Override
    public ServiceResult deleteTopicCommentByBelongTopicId(Long belongTopicId) {
        List<Long> topicCommentIdList = topicCommentRepository.findAllTopicCommentIdByBelongTopicId(belongTopicId);
        for (Long id : topicCommentIdList) {
            topicReplyService.deleteTopicReplyByBelongCommentId(id);
            evictFromCache(entityName() + id);
        }
        topicCommentRepository.deleteAllByBelongTopicId(belongTopicId);
        return ServiceResult.successResultWithMesage("delete topic comment success");
    }

    @Override
    public ServiceResult createTopicComment(TopicComment topicComment, Map<String, byte[]> images) {
        if (StringUtils.isBlank(topicComment.getContent())) {
            return recordErrorAndCreateFailResultWithMessage("create topicComment error: content is empty");
        }
        if (topicComment.getAuthorId() == null) {
            return recordErrorAndCreateFailResultWithMessage("create topicComment error: authorId is null");
        }
        if (topicComment.getBelongTopicId() == null || !forumTopicRepository.existsById(topicComment.getBelongTopicId())) {
            return recordErrorAndCreateFailResultWithMessage("create topicComment error: belongTopicId is null or not exists");
        }
        if (!ImageUtils.storeImages(images)) {
            return recordErrorAndCreateFailResultWithMessage("create topicComment error: store image failed");
        }
        topicComment.getImagePaths().clear();
        topicComment.getImagePaths().addAll(images.keySet());
        Optional<TopicComment> topicCommentOptional = create(topicComment);
        if (!topicCommentOptional.isPresent()) {
            ImageUtils.deleteImages(images.keySet());
            return recordErrorAndCreateFailResultWithMessage("create topicComment error: create topicComment failed");
        }
        topicComment = topicCommentOptional.get();
        return ServiceResult.successResult("create topicComment success", TopicComment.class, topicComment);
    }

    @Override
    public ServiceResult updateTopicComment(TopicComment topicComment, Map<String, byte[]> images) {
        if (StringUtils.isBlank(topicComment.getContent())) {
            return recordErrorAndCreateFailResultWithMessage("update topicComment error: content is empty");
        }
        if (topicComment.getAuthorId() == null) {
            return recordErrorAndCreateFailResultWithMessage("update topicComment error: authorId is null");
        }
        if (topicComment.getBelongTopicId() == null || !forumTopicRepository.existsById(topicComment.getBelongTopicId())) {
            return recordErrorAndCreateFailResultWithMessage("update topicComment error: belongTopicId is null or not exists");
        }
        List<String> oldImagePaths = new LinkedList<>(topicComment.getImagePaths());
        if (images != null) {
            if (!ImageUtils.storeImages(images)) {
                return recordErrorAndCreateFailResultWithMessage("update topicComment error: store image failed");
            }
            topicComment.getImagePaths().clear();
            topicComment.getImagePaths().addAll(images.keySet());
        }
        Optional<TopicComment> topicCommentOptional = update(topicComment);
        if (!topicCommentOptional.isPresent()) {
            if (images != null) {
                ImageUtils.deleteImages(images.keySet());
            }
            topicComment.getImagePaths().clear();
            topicComment.getImagePaths().addAll(oldImagePaths);
            return recordErrorAndCreateFailResultWithMessage("update topicComment error: update topicComment failed");
        }
        if (images != null) {
            ImageUtils.deleteImages(oldImagePaths);
        }
        topicComment = topicCommentOptional.get();
        return ServiceResult.successResult("update topicComment success", TopicComment.class, topicComment);
    }

    @Override
    public ServiceResult findAllTopicComment(Long belongTopicId) {
        if (belongTopicId == null) {
            return recordWarnAndCreateSuccessResultWithMessage("find all topicComment warn: belongTopicId is null")
                    .merge(ServiceResult.successResultWithExtra(List.class, new LinkedList<TopicComment>()), true);
        }
        List<com.corkili.learningserver.po.TopicComment> allTopicCommentPO =
                topicCommentRepository.findAllByBelongTopicId(belongTopicId);
        List<TopicComment> allTopicComment = new LinkedList<>();
        StringBuilder errId = new StringBuilder();
        int i = 0;
        for (com.corkili.learningserver.po.TopicComment topicCommentPO : allTopicCommentPO) {
            Optional<TopicComment> topicCommentOptional = po2bo(topicCommentPO);
            i++;
            if (!topicCommentOptional.isPresent()) {
                errId.append(topicCommentPO.getId());
                if (i != allTopicCommentPO.size()) {
                    errId.append(",");
                }
            } else {
                TopicComment topicComment = topicCommentOptional.get();
                allTopicComment.add(topicComment);
                // cache
                putToCache(entityName() + topicComment.getId(), topicComment);
            }
        }
        String msg = "find all topicComment success";
        if (errId.length() != 0) {
            msg = ServiceUtils.format("find all topicComment warn: transfer topicComment po [{}] to bo failed.", errId.toString());
            log.warn(msg);
        }
        return ServiceResult.successResult(msg, List.class, allTopicComment);
    }

}
