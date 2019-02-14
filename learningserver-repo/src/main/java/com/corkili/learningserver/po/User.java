package com.corkili.learningserver.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
@Table(name = "t_user")
@SQLDelete(sql = "update t_user set " + POConstant.KEY_DELETED + " = " + POConstant.DELETED + " where id = ?")
@SQLDeleteAll(sql = "update t_user set " + POConstant.KEY_DELETED + " = " + POConstant.DELETED + " where id = ?")
@Where(clause = POConstant.KEY_DELETED + " = " + POConstant.EXISTED)
@WhereJoinTable(clause = POConstant.KEY_DELETED + " = " + POConstant.EXISTED)
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class User {

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

    @Column(name = "phone", unique = true, nullable = false, length = 16)
    @NotBlank
    @Size(min = 1, max = 16)
    private String phone;

    @Column(name = "password", nullable = false, length = 128)
    @NotBlank
    @Size(min = 6, max = 128)
    private String password;

    @Column(name = "username", nullable = false, length = 50)
    @NotBlank
    @Size(min = 1, max = 50)
    private String username;

    @Column(name = "user_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Type userType;

    public boolean isTeacher() {
        return userType == Type.Teacher;
    }

    public boolean isStudent() {
        return userType == Type.Student;
    }

    public enum Type {
        Teacher,
        Student
    }

}
