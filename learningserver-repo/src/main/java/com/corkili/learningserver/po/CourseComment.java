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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.corkili.learningserver.common.POConstant;

@Entity
@Table(name = "t_course_comment")
@SQLDelete(sql = "update t_course_comment set " + POConstant.KEY_DELETED + " = " + POConstant.DELETED + " where id = ?")
@SQLDeleteAll(sql = "update t_course_comment set " + POConstant.KEY_DELETED + " = " + POConstant.DELETED + " where id = ?")
@Where(clause = POConstant.KEY_DELETED + " = " + POConstant.EXISTED)
@WhereJoinTable(clause = POConstant.KEY_DELETED + " = " + POConstant.EXISTED)
@Getter
@Setter
@ToString
public class CourseComment {

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

    @Column(name = "comment_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type commentType;

    @Column(name = "content", nullable = false, length = 1000)
    @NotBlank
    private String content;

    /**
     * Format:
     *   use {!!!} divide each image path.
     */
    @Column(name = "image_paths", length = 10000)
    private String imagePaths;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "comment_author_fk", nullable = false)
    private User commentAuthor;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "commented_course", nullable = false)
    private Course commentedCourse;

    public boolean isVeryGoodComment() {
        return commentType == Type.VERY_GOOD;
    }

    public boolean isGoodComment() {
        return commentType == Type.GOOD;
    }

    public boolean isMidComment() {
        return commentType == Type.MID;
    }

    public boolean isJustMidComment() {
        return commentType == Type.JUST_MID;
    }

    public boolean isBadComment() {
        return commentType == Type.BAD;
    }

    public enum Type {
        VERY_GOOD,
        GOOD,
        MID,
        JUST_MID,
        BAD
    }
}
