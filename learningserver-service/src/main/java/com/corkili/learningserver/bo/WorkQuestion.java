package com.corkili.learningserver.bo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkQuestion {

    private Long id;

    private Date createTime;

    private Date updateTime;

    private int index;

    private CourseWork belongCourseWork;

    private Question question;

}
