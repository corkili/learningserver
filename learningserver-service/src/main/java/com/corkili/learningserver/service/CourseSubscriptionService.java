package com.corkili.learningserver.service;

import com.corkili.learningserver.bo.CourseSubscription;
import com.corkili.learningserver.common.ServiceResult;

public interface CourseSubscriptionService extends Service<CourseSubscription, com.corkili.learningserver.po.CourseSubscription> {

    ServiceResult createCourseSubscription(CourseSubscription courseSubscription);

    ServiceResult findAllCourseSubscription(Long subscriberId, Long subscribedCourseId);

    ServiceResult deleteCourseSubscription(Long courseSubscriptionId);

    ServiceResult deleteCourseSubscriptionBySubscribedCourseId(Long subscribedCourseId);

}
