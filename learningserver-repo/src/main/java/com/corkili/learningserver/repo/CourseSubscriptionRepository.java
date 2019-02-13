package com.corkili.learningserver.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.corkili.learningserver.po.CourseSubscription;

public interface CourseSubscriptionRepository extends JpaRepository<CourseSubscription, Long> {

}
