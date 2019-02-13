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
import javax.persistence.OneToOne;
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
@Table(name = "t_course")
@SQLDelete(sql = "update t_course set " + POConstant.KEY_DELETED + " = " + POConstant.DELETED + " where id = ?")
@SQLDeleteAll(sql = "update t_course set " + POConstant.KEY_DELETED + " = " + POConstant.DELETED + " where id = ?")
@Where(clause = POConstant.KEY_DELETED + " = " + POConstant.EXISTED)
@WhereJoinTable(clause = POConstant.KEY_DELETED + " = " + POConstant.EXISTED)
@Getter
@Setter
@ToString
public class Course {

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

    @Column(name = "course_name", nullable = false, length = 50)
    @NotBlank
    private String courseName;

    @Column(name = "description", nullable = false, length = 2000)
    @NotBlank
    private String description;

    /**
     * Format:
     *   use {!!!} divide each image path.
     */
    @Column(name = "image_paths", length = 10000)
    private String imagePaths;

    /**
     * format: use ; divide each tag
     *
     *   tag1;tag2;tag3
     */
    @Column(name = "tags", nullable = false, length = 2000)
    @NotBlank
    private String tags;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "category_fk", nullable = false)
    private CourseCategory category;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "teacher_fk", nullable = false)
    private User teacher;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "scorm_fk")
    private Scorm courseware;

}
