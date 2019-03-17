package com.corkili.learningserver.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.CourseSubscription;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.common.ServiceUtils;
import com.corkili.learningserver.repo.CourseRepository;
import com.corkili.learningserver.repo.CourseSubscriptionRepository;
import com.corkili.learningserver.repo.UserRepository;
import com.corkili.learningserver.service.CourseSubscriptionService;

@Slf4j
@Service
@Transactional
public class CourseSubscriptionServiceImpl extends ServiceImpl<CourseSubscription, com.corkili.learningserver.po.CourseSubscription> implements CourseSubscriptionService {

    @Autowired
    private CourseSubscriptionRepository courseSubscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    protected JpaRepository<com.corkili.learningserver.po.CourseSubscription, Long> repository() {
        return courseSubscriptionRepository;
    }

    @Override
    protected String entityName() {
        return "courseSubscription";
    }

    @Override
    protected CourseSubscription newBusinessObject() {
        return new CourseSubscription();
    }

    @Override
    protected com.corkili.learningserver.po.CourseSubscription newPersistObject() {
        return new com.corkili.learningserver.po.CourseSubscription();
    }

    @Override
    protected Logger logger() {
        return log;
    }

    @Override
    public ServiceResult createCourseSubscription(CourseSubscription courseSubscription) {
        if (courseSubscription.getSubscriberId() == null) {
            return recordErrorAndCreateFailResultWithMessage("create courseSubscription error: subscriberId is null");
        }
        if (courseSubscription.getSubscribedCourseId() == null) {
            return recordErrorAndCreateFailResultWithMessage("create courseSubscription error: subscribedCourseId is null");
        }
        if (!userRepository.existsById(courseSubscription.getSubscriberId())) {
            return recordErrorAndCreateFailResultWithMessage("create courseSubscription error: subscriber (user[{}]) not exist",
                    courseSubscription.getSubscriberId());
        }
        if (!courseRepository.existsById(courseSubscription.getSubscribedCourseId())) {
            return recordErrorAndCreateFailResultWithMessage("create courseSubscription error: subscribedCourse (course[{}]) not exist",
                    courseSubscription.getSubscribedCourseId());
        }
        if (courseSubscriptionRepository.existsBySubscriberIdAndSubscribedCourseId(
                courseSubscription.getSubscriberId(), courseSubscription.getSubscribedCourseId())) {
            return recordErrorAndCreateFailResultWithMessage("create courseSubscription error: user [{}] already subscribe course [{}]",
                    courseSubscription.getSubscriberId(), courseSubscription.getSubscribedCourseId());
        }
        Optional<CourseSubscription> courseSubscriptionOptional = create(courseSubscription);
        if (!courseSubscriptionOptional.isPresent()) {
            return recordErrorAndCreateFailResultWithMessage("create courseSubscription error: create courseSubscription failed");
        }
        courseSubscription = courseSubscriptionOptional.get();
        return ServiceResult.successResult("create courseSubscription success", CourseSubscription.class, courseSubscription);
    }

    @Override
    public ServiceResult findAllCourseSubscription(Long subscriberId, Long subscribedCourseId) {
        String msg = "find all courseSubscription success";
        List<com.corkili.learningserver.po.CourseSubscription> allCourseSubscriptionPO = new LinkedList<>();
        if (subscriberId != null && subscribedCourseId != null) {
            allCourseSubscriptionPO.addAll(courseSubscriptionRepository.findAllBySubscriberIdAndSubscribedCourseId(subscriberId, subscribedCourseId));
        } else if (subscriberId != null) {
            allCourseSubscriptionPO.addAll(courseSubscriptionRepository.findAllBySubscriberId(subscriberId));
        } else if (subscribedCourseId != null){
            allCourseSubscriptionPO.addAll(courseSubscriptionRepository.findAllBySubscribedCourseId(subscribedCourseId));
        }
        List<CourseSubscription> allCourseSubscription = new ArrayList<>(allCourseSubscriptionPO.size());
        StringBuilder errId = new StringBuilder();
        int i = 0;
        for (com.corkili.learningserver.po.CourseSubscription courseSubscriptionPO : allCourseSubscriptionPO) {
            Optional<CourseSubscription> courseSubscriptionOptional = po2bo(courseSubscriptionPO);
            i++;
            if (!courseSubscriptionOptional.isPresent()) {
                errId.append(courseSubscriptionPO.getId());
                if (i != allCourseSubscriptionPO.size()) {
                    errId.append(",");
                }
            } else {
                CourseSubscription courseSubscription = courseSubscriptionOptional.get();
                allCourseSubscription.add(courseSubscription);
                // cache
                putToCache(entityName() + courseSubscription.getId(), courseSubscription);
            }
        }
        if (errId.length() != 0) {
            msg = ServiceUtils.format("find all courseSubscription warn: transfer course po [{}] to bo failed.", errId.toString());
            log.warn(msg);
        }
        return ServiceResult.successResult(msg, List.class, allCourseSubscription);
    }

    @Override
    public ServiceResult deleteCourseSubscription(Long courseSubscriptionId) {
        if (!delete(courseSubscriptionId)) {
            return recordWarnAndCreateSuccessResultWithMessage("delete course subscription success");
        }
        return ServiceResult.successResultWithMesage("delete course subscription success");
    }

    @Override
    public ServiceResult deleteCourseSubscriptionBySubscribedCourseId(Long subscribedCourseId) {
        List<Long> courseSubscriptionIdList = courseSubscriptionRepository.findAllCourseSubscriptionIdBySubscribedCourseId(subscribedCourseId);
        courseSubscriptionRepository.deleteAllBySubscribedCourseId(subscribedCourseId);
        for (Long id : courseSubscriptionIdList) {
            evictFromCache(entityName() + id);
        }
        return ServiceResult.successResultWithMesage("delete course subscription success");
    }
}
