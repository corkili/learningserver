package com.corkili.learningserver.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.CourseSubscription;
import com.corkili.learningserver.common.ServiceResult;
import com.corkili.learningserver.repo.CourseSubscriptionRepository;
import com.corkili.learningserver.service.CourseSubscriptionService;

@Slf4j
@Service
public class CourseSubscriptionServiceImpl extends ServiceImpl<CourseSubscription, com.corkili.learningserver.po.CourseSubscription> implements CourseSubscriptionService {

    @Autowired
    private CourseSubscriptionRepository courseSubscriptionRepository;

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
