package com.corkili.learningserver.po;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "t_question")
@SQLDelete(sql = "update t_question set " + POConstant.KEY_DELETED + " = " + POConstant.DELETED + " where id = ?")
@SQLDeleteAll(sql = "update t_question set " + POConstant.KEY_DELETED + " = " + POConstant.DELETED + " where id = ?")
@Where(clause = POConstant.KEY_DELETED + " = " + POConstant.EXISTED)
@WhereJoinTable(clause = POConstant.KEY_DELETED + " = " + POConstant.EXISTED)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "create_time", updatable = false, nullable = false,
            columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createTime;

    @Column(name = "update_time", nullable = false,
            columnDefinition = "timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date updateTime;

    @Column(name = POConstant.KEY_DELETED, nullable = false)
    @Range(min = POConstant.EXISTED, max = POConstant.DELETED)
    private byte deleted;

    @Column(name = "question", nullable = false, length = 10000)
    @NotBlank
    private String question;

    /**
     * Format:
     *   use {!!!} divide each image path.
     */
    @Column(name = "image_paths", length = 10000)
    private String imagePaths;

    @Column(name = "question_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type questionType;

    @Column(name = "auto_check", nullable = false)
    private boolean autoCheck;

    /**
     * When questionType is SingleChoice or MultipleChoice, use the attribute store choices.
     *
     * String Format, use {###} divide each choice, use {@@@} divide choiceIndex and content in each choice:
     *
     *   choiceIndex{@@@}content{###}choiceIndex{@@@}content ...
     *
     * Example:
     *
     *   1{@@@}choice A{###}2{@@@}choice B{###}3{@@@}choice C
     *
     */
    @Column(name = "choices", length = 2000)
    @NotBlank
    private String choices;

    /**
     * Use different format to represent different answer, depend on questionType:
     *
     * 1. SingleFilling:
     *   use {$$$} divide each correctAnswer, if the response from student is one of the correctAnswer, response is correct:
     *
     *   correctAnswer1{$$$}correctAnswer2{$$$}correctAnswer3
     *
     * 2. MultipleFilling:
     *   use {***} divide each answer, use {%%%} divide answerIndex and answerContent in each answer, 
     *   format of answerContent use the format of SingleFilling:
     *
     *   answerIndex{%%%}answerContent{***}answerIndex{%%%}answerContent
     *
     * 3. SingleChoice:
     *   a integer represent the correct choice index described in choices attribute.
     *   
     * 4. MultipleChoice:
     *   use {|||} divide each correctChoice if get all score when select all correctChoice and get half score when
     *   select part of these correctChoice, use {&&&} divide each correctChoice if get all score only when select all correctChoice.
     *
     *   correctChoice1{|||}correctChoice2{|||}correctChoice3
     *
     *   correctChoice1{&&&}correctChoice2{&&&}correctChoice3
     *
     * 5. Essay:
     *   a string represent the correct answer. if contains image(s), use {##image##} divide text and imagePaths,
     *   then use {!!!} divide each image path in imagePaths
     *
     */
    @Column(name = "answer", nullable = false, length = 10000)
    @NotBlank
    private String answer;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "author_fk", nullable = false)
    private User author;

    public boolean isSingleFillingQuestion() {
        return questionType == Type.SingleFilling;
    }

    public boolean isMultipleFillingQuestion() {
        return questionType == Type.MultipleFilling;
    }

    public boolean isFillingQuestion() {
        return questionType == Type.SingleFilling || questionType == Type.MultipleFilling;
    }

    public boolean isSingleChoiceQuestion() {
        return questionType == Type.SingleChoice;
    }

    public boolean isMultipleChoiceQuestion() {
        return questionType == Type.MultipleChoice;
    }

    public boolean isChoiceQuestion() {
        return questionType == Type.SingleChoice || questionType == Type.MultipleChoice;
    }

    public boolean isEssayQuestion() {
        return questionType == Type.Essay;
    }

    public enum Type {
        SingleFilling, // auto check is true or false
        MultipleFilling, // auto check is true or false
        SingleChoice, // auto check must be true
        MultipleChoice, // auto check must be true
        Essay // auto check must be false
    }

}
