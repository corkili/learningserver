package com.corkili.learningserver.bo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseSubscription {

    private Long id;

    private Date createTime;

    private Date updateTime;

    private User subscriber;

    private Course subscribedCourse;

}
