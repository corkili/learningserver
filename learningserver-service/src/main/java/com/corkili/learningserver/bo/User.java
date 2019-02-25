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
public class User implements BusinessObject {

    private Long id;

    private Date createTime;

    private Date updateTime;

    private String phone;

    private String username;

    private String password;

    private boolean rawPassword;

    private Type userType;

    public static User copyFrom(User user) {
        User copyUser = new User();
        copyUser.id = user.id;
        copyUser.createTime = user.createTime;
        copyUser.updateTime = user.updateTime;
        copyUser.phone = user.phone;
        copyUser.username = user.username;
        copyUser.password = user.password;
        copyUser.rawPassword = user.rawPassword;
        copyUser.userType = user.userType;
        return copyUser;
    }

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
