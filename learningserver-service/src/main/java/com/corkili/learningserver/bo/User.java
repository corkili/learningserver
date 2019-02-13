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
public class User {

    private Long id;

    private Date createTime;

    private Date updateTime;

    private String phone;

    private String username;

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
