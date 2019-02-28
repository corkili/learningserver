package com.corkili.learningserver.service.impl;

import com.corkili.learningserver.bo.TopicReply;
import com.corkili.learningserver.common.ImageUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.repo.TopicCommentRepository;
import com.corkili.learningserver.repo.TopicReplyRepository;
import com.corkili.learningserver.service.TopicReplyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class TopicReplyServiceImpl extends ServiceImpl<TopicReply, com.corkili.learningserver.po.TopicReply> implements TopicReplyService {

    @Autowired
    private TopicReplyRepository topicReplyRepository;

    @Autowired
    private TopicCommentRepository topicCommentRepository;

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

    @Override
    public ServiceResult deleteTopicReplyByBelongCommentId(Long belongCommentId) {
        List<Long> topicReplyIdList = topicReplyRepository.findAllTopicReplyIdByBelongCommentId(belongCommentId);
        topicReplyRepository.deleteAllByBelongCommentId(belongCommentId);
        for (Long id : topicReplyIdList) {
            evictFromCache(entityName() + id);
        }
        return ServiceResult.successResultWithMesage("delete topic reply success");
    }

    @Override
    public ServiceResult createTopicReply(TopicReply topicReply, Map<String, byte[]> images) {
        if (StringUtils.isBlank(topicReply.getContent())) {
            return recordErrorAndCreateFailResultWithMessage("create topicReply error: content is empty");
        }
        if (topicReply.getAuthorId() == null) {
            return recordErrorAndCreateFailResultWithMessage("create topicReply error: authorId is null");
        }
        if (topicReply.getBelongCommentId() == null || !topicCommentRepository.existsById(topicReply.getBelongCommentId())) {
            return recordErrorAndCreateFailResultWithMessage("create topicReply error: belongCommentId is null or not exists");
        }
        if (ImageUtils.storeImages(images)) {
            return recordErrorAndCreateFailResultWithMessage("create topicReply error: store image failed");
        }
        topicReply.getImagePaths().clear();
        topicReply.getImagePaths().addAll(images.keySet());
        Optional<TopicReply> topicReplyOptional = create(topicReply);
        if (!topicReplyOptional.isPresent()) {
            ImageUtils.deleteImages(images.keySet());
            return recordErrorAndCreateFailResultWithMessage("create topicReply error: create topicReply failed");
        }
        topicReply = topicReplyOptional.get();
        return ServiceResult.successResult("create topicReply success", TopicReply.class, topicReply);
    }

    @Override
    public ServiceResult updateTopicReply(TopicReply topicReply, Map<String, byte[]> images) {
        if (StringUtils.isBlank(topicReply.getContent())) {
            return recordErrorAndCreateFailResultWithMessage("update topicReply error: content is empty");
        }
        if (topicReply.getAuthorId() == null) {
            return recordErrorAndCreateFailResultWithMessage("update topicReply error: authorId is null");
        }
        if (topicReply.getBelongCommentId() == null || !topicCommentRepository.existsById(topicReply.getBelongCommentId())) {
            return recordErrorAndCreateFailResultWithMessage("update topicReply error: belongCommentId is null or not exists");
        }
        List<String> oldImagePaths = new LinkedList<>(topicReply.getImagePaths());
        if (images != null) {
            if (ImageUtils.storeImages(images)) {
                return recordErrorAndCreateFailResultWithMessage("update topicReply error: store image failed");
            }
            topicReply.getImagePaths().clear();
            topicReply.getImagePaths().addAll(images.keySet());
        }
        Optional<TopicReply> topicReplyOptional = update(topicReply);
        if (!topicReplyOptional.isPresent()) {
            if (images != null) {
                ImageUtils.deleteImages(images.keySet());
            }
            topicReply.getImagePaths().clear();
            topicReply.getImagePaths().addAll(oldImagePaths);
            return recordErrorAndCreateFailResultWithMessage("update topicReply error: update topicReply failed");
        }
        if (images != null) {
            ImageUtils.deleteImages(oldImagePaths);
        }
        topicReply = topicReplyOptional.get();
        return ServiceResult.successResult("update topicReply success", TopicReply.class, topicReply);
    }

    @Override
    public ServiceResult findAllTopicReply(Long belongCommentId) {
        if (belongCommentId == null) {
            return recordWarnAndCreateSuccessResultWithMessage("find all topicReply warn: belongCommentId is null")
                    .merge(ServiceResult.successResultWithExtra(List.class, new LinkedList<TopicReply>()), true);
        }
        List<com.corkili.learningserver.po.TopicReply> allTopicReplyPO =
                topicReplyRepository.findAllByBelongCommentId(belongCommentId);
        List<TopicReply> allTopicReply = new LinkedList<>();
        StringBuilder errId = new StringBuilder();
        int i = 0;
        for (com.corkili.learningserver.po.TopicReply topicReplyPO : allTopicReplyPO) {
            Optional<TopicReply> topicReplyOptional = po2bo(topicReplyPO);
            i++;
            if (!topicReplyOptional.isPresent()) {
                errId.append(topicReplyPO.getId());
                if (i != allTopicReplyPO.size()) {
                    errId.append(",");
                }
            } else {
                TopicReply topicReply = topicReplyOptional.get();
                allTopicReply.add(topicReply);
                // cache
                putToCache(entityName() + topicReply.getId(), topicReply);
            }
        }
        String msg = "find all topicReply success";
        if (errId.length() != 0) {
            msg = ServiceUtils.format("find all topicReply warn: transfer topicReply po [{}] to bo failed.", errId.toString());
            log.warn(msg);
        }
        return ServiceResult.successResult(msg, List.class, allTopicReply);
    }
}
