package com.corkili.learningserver.bo;

import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CourseSubscription implements BusinessObject {

    private Long id;

    private Date createTime;

    private Date updateTime;

    private Long subscriberId;

    private Long subscribedCourseId;

}
