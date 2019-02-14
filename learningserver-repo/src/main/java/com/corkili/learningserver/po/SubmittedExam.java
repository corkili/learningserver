package com.corkili.learningserver.po;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLDeleteAll;
import org.hibernate.annotations.Where;
import org.hibernate.annotations.WhereJoinTable;
import org.hibernate.validator.constraints.Range;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.corkili.learningserver.common.POConstant;

@Entity
@Table(name = "t_submitted_exam")
@SQLDelete(sql = "update t_submitted_exam set " + POConstant.KEY_DELETED + " = " + POConstant.DELETED + " where id = ?")
@SQLDeleteAll(sql = "update t_submitted_exam set " + POConstant.KEY_DELETED + " = " + POConstant.DELETED + " where id = ?")
@Where(clause = POConstant.KEY_DELETED + " = " + POConstant.EXISTED)
@WhereJoinTable(clause = POConstant.KEY_DELETED + " = " + POConstant.EXISTED)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SubmittedExam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "create_time", updatable = false, nullable = false,
            columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "update_time", nullable = false,
            columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updateTime;

    @Column(name = POConstant.KEY_DELETED, nullable = false)
    @Range(min = POConstant.EXISTED, max = POConstant.DELETED)
    private byte deleted;

    /**
     * Format:
     *   use {^^^} divide each question's submitted answer
     *
     *   use {###} divide question index, submitted answer, score(negative if not checked)
     *
     *   use {***} divide each filling answer in submitted answer,
     *   use {%%%} divide answerIndex and answerContent in each filling answer, if questionType is MultipleFilling
     *
     *   use {&&&} divide choice in submitted answer, if questionType is MultipleChoice
     *
     *   when type is essay, a string represent the correct answer. if contains image(s),
     *   use {##image##} divide text and imagePaths, then use {!!!} divide each image path in imagePaths
     */
    @Column(name = "submitted_answers", nullable = false, length = 10000)
    @NotBlank
    private String submittedAnswers;

    @Column(name = "already_check_all_answer", nullable = false)
    private boolean alreadyCheckAllAnswer;

    @Column(name = "totalScore", nullable = false)
    @Range(min = 0)
    private Double totalScore;

    @ManyToOne(optional = false)
    @JoinColumn(name = "belong_exam_fk", nullable = false)
    private Exam belongExam;

    @ManyToOne(optional = false)
    @JoinColumn(name = "submitter_fk", nullable = false)
    private User submitter;

}
