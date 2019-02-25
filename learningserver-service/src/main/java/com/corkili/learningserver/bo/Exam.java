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

    public static Exam copyFrom(Exam exam) {
        Exam copyExam = new Exam();
        copyExam.id = exam.id;
        copyExam.createTime = exam.createTime;
        copyExam.updateTime = exam.updateTime;
        copyExam.examName = exam.examName;
        copyExam.belongCourseId = exam.belongCourseId;
        copyExam.startTime = exam.startTime;
        copyExam.endTime = exam.endTime;
        copyExam.duration = exam.duration;
        return copyExam;
    }

}
