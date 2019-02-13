package com.corkili.learningserver.bo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseWork {

    private Long id;

    private Date createTime;

    private Date updateTime;

    private String workName;

    private Course belongCourse;

    private Date deadline;

}
