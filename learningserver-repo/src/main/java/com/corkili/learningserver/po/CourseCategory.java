package com.corkili.learningserver.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "t_course_category")
@SQLDelete(sql = "update t_course_category set " + POConstant.KEY_DELETED + " = " + POConstant.DELETED + " where id = ?")
@SQLDeleteAll(sql = "update t_course_category set " + POConstant.KEY_DELETED + " = " + POConstant.DELETED + " where id = ?")
@Where(clause = POConstant.KEY_DELETED + " = " + POConstant.EXISTED)
@WhereJoinTable(clause = POConstant.KEY_DELETED + " = " + POConstant.EXISTED)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class CourseCategory {

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

    @Column(name = "category_name", nullable = false, length = 50)
    @NotBlank
    private String categoryName;

}
