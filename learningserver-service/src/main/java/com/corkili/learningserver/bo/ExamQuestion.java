package com.corkili.learningserver.bo;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamQuestion {

    private Long id;

    private Date createTime;

    private Date updateTime;

    private int index;

    private Exam belongExam;

    private Question question;

    private Double score;
}
