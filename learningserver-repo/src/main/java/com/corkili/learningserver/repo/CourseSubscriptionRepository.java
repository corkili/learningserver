package com.corkili.learningserver.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.corkili.learningserver.po.CourseSubscription;

public interface CourseSubscriptionRepository extends JpaRepository<CourseSubscription, Long> {

    @Query("select cs.id from CourseSubscription cs left join cs.subscribedCourse cssc where cssc.id = ?1")
    List<Long> findAllCourseSubscriptionIdBySubscribedCourseId(Long subscribedCourseId);

    void deleteAllBySubscribedCourseId(Long subscribedCourseId);

}
