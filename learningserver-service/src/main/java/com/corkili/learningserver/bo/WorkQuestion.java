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
public class WorkQuestion {

    private Long id;

    private Date createTime;

    private Date updateTime;

    private int index;

    private CourseWork belongCourseWork;

    private Question question;

}
