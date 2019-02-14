package com.corkili.learningserver.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.bo.CourseSubscription;
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
}
