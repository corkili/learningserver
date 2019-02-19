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
public class Exam implements BusinessObject {

    private Long id;

    private Date createTime;

    private Date updateTime;

    private String examName;

    private Long belongCourseId;

    private Date startTime;

    private Date endTime;

    private Integer duration;

}
