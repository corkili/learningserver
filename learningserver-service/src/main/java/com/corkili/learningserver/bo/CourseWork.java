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
public class CourseWork implements BusinessObject {

    private Long id;

    private Date createTime;

    private Date updateTime;

    private boolean open;

    private String workName;

    private Long belongCourseId;

    private Date deadline;

    public static CourseWork copyFrom(CourseWork courseWork) {
        CourseWork copyCourseWork = new CourseWork();
        copyCourseWork.id = courseWork.id;
        copyCourseWork.createTime = courseWork.createTime;
        copyCourseWork.updateTime = courseWork.updateTime;
        copyCourseWork.open = courseWork.open;
        copyCourseWork.workName = courseWork.workName;
        copyCourseWork.belongCourseId = courseWork.belongCourseId;
        copyCourseWork.deadline = courseWork.deadline;
        return copyCourseWork;
    }

}
