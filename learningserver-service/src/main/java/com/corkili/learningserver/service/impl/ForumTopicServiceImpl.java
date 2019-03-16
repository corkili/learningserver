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

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.ForumTopic;
import com.corkili.learningserver.common.ImageUtils;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.repo.CourseRepository;
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

    @Autowired
    private CourseRepository courseRepository;

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
        serviceResult = serviceResult.merge(topicCommentService.deleteTopicCommentByBelongTopicId(forumTopicId), true);
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

    @Override
    public ServiceResult createForumTopic(ForumTopic forumTopic, Map<String, byte[]> images) {
        if (StringUtils.isBlank(forumTopic.getTitle())) {
            return recordErrorAndCreateFailResultWithMessage("create forumTopic error: title is empty");
        }
        if (StringUtils.isBlank(forumTopic.getDescription())) {
            return recordErrorAndCreateFailResultWithMessage("create forumTopic error: description is empty");
        }
        if (forumTopic.getAuthorId() == null) {
            return recordErrorAndCreateFailResultWithMessage("create forumTopic error: authorId is null");
        }
        if (forumTopic.getBelongCourseId() == null || !courseRepository.existsById(forumTopic.getBelongCourseId())) {
            return recordErrorAndCreateFailResultWithMessage("create forumTopic error: belongCourseId is null or not exists");
        }
        if (!ImageUtils.storeImages(images)) {
            return recordErrorAndCreateFailResultWithMessage("create forumTopic error: store image failed");
        }
        forumTopic.getImagePaths().clear();
        forumTopic.getImagePaths().addAll(images.keySet());
        Optional<ForumTopic> forumTopicOptional = create(forumTopic);
        if (!forumTopicOptional.isPresent()) {
            ImageUtils.deleteImages(images.keySet());
            return recordErrorAndCreateFailResultWithMessage("create forumTopic error: create forumTopic failed");
        }
        forumTopic = forumTopicOptional.get();
        return ServiceResult.successResult("create forumTopic success", ForumTopic.class, forumTopic);
    }

    @Override
    public ServiceResult updateForumTopic(ForumTopic forumTopic, Map<String, byte[]> images) {
        if (StringUtils.isBlank(forumTopic.getTitle())) {
            return recordErrorAndCreateFailResultWithMessage("update forumTopic error: title is empty");
        }
        if (StringUtils.isBlank(forumTopic.getDescription())) {
            return recordErrorAndCreateFailResultWithMessage("update forumTopic error: description is empty");
        }
        if (forumTopic.getAuthorId() == null) {
            return recordErrorAndCreateFailResultWithMessage("update forumTopic error: authorId is null");
        }
        if (forumTopic.getBelongCourseId() == null || !courseRepository.existsById(forumTopic.getBelongCourseId())) {
            return recordErrorAndCreateFailResultWithMessage("update forumTopic error: belongCourseId is null or not exists");
        }
        List<String> oldImagePaths = new LinkedList<>(forumTopic.getImagePaths());
        if (images != null) {
            if (!ImageUtils.storeImages(images)) {
                return recordErrorAndCreateFailResultWithMessage("update forumTopic error: store image failed");
            }
            forumTopic.getImagePaths().clear();
            forumTopic.getImagePaths().addAll(images.keySet());
        }
        Optional<ForumTopic> forumTopicOptional = update(forumTopic);
        if (!forumTopicOptional.isPresent()) {
            if (images != null) {
                ImageUtils.deleteImages(images.keySet());
            }
            forumTopic.getImagePaths().clear();
            forumTopic.getImagePaths().addAll(oldImagePaths);
            return recordErrorAndCreateFailResultWithMessage("update forumTopic error: update forumTopic failed");
        }
        if (images != null) {
            ImageUtils.deleteImages(oldImagePaths);
        }
        forumTopic = forumTopicOptional.get();
        return ServiceResult.successResult("update forumTopic success", ForumTopic.class, forumTopic);
    }

    @Override
    public ServiceResult findAllForumTopic(Long belongCourseId) {
        if (belongCourseId == null) {
            return recordWarnAndCreateSuccessResultWithMessage("find all forumTopic warn: belongCourseId is null")
                    .merge(ServiceResult.successResultWithExtra(List.class, new LinkedList<ForumTopic>()), true);
        }
        List<com.corkili.learningserver.po.ForumTopic> allForumTopicPO =
                forumTopicRepository.findAllByBelongCourseId(belongCourseId);
        List<ForumTopic> allForumTopic = new LinkedList<>();
        StringBuilder errId = new StringBuilder();
        int i = 0;
        for (com.corkili.learningserver.po.ForumTopic forumTopicPO : allForumTopicPO) {
            Optional<ForumTopic> forumTopicOptional = po2bo(forumTopicPO);
            i++;
            if (!forumTopicOptional.isPresent()) {
                errId.append(forumTopicPO.getId());
                if (i != allForumTopicPO.size()) {
                    errId.append(",");
                }
            } else {
                ForumTopic forumTopic = forumTopicOptional.get();
                allForumTopic.add(forumTopic);
                // cache
                putToCache(entityName() + forumTopic.getId(), forumTopic);
            }
        }
        String msg = "find all forumTopic success";
        if (errId.length() != 0) {
            msg = ServiceUtils.format("find all forumTopic warn: transfer forumTopic po [{}] to bo failed.", errId.toString());
            log.warn(msg);
        }
        return ServiceResult.successResult(msg, List.class, allForumTopic);
    }


}
