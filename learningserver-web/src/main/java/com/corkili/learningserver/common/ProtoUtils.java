package com.corkili.learningserver.common;

import java.util.LinkedList;
import java.util.List;

import com.google.protobuf.ByteString;

import com.corkili.learningserver.bo.Course;
import com.corkili.learningserver.bo.User;
import com.corkili.learningserver.generate.protobuf.Info.CourseInfo;
import com.corkili.learningserver.generate.protobuf.Info.Image;
import com.corkili.learningserver.generate.protobuf.Info.UserInfo;
import com.corkili.learningserver.generate.protobuf.Info.UserType;

public class ProtoUtils {

    public static CourseInfo generateCourseInfo(Course course, User teacher, boolean loadImageData) {
        List<Image> imageList = new LinkedList<>();
        for (String imagePath : course.getImagePaths()) {
            if (loadImageData) {
                imageList.add(Image.newBuilder()
                        .setPath(imagePath)
                        .setHasData(true)
                        .setImage(ByteString.copyFrom(ImageUtils.readImage(imagePath)))
                        .build());
            } else {
                imageList.add(Image.newBuilder()
                        .setPath(imagePath)
                        .setHasData(false)
                        .build());
            }
        }
        return CourseInfo.newBuilder()
                .setCreateTime(course.getCreateTime().getTime())
                .setUpdateTime(course.getUpdateTime().getTime())
                .setCourseName(course.getCourseName())
                .setDescription(course.getDescription())
                .addAllImage(imageList)
                .addAllTag(course.getTags())
                .setTeacherInfo(generateUserInfo(teacher))
                .build();
    }

    public static UserInfo generateUserInfo(User user) {
        return UserInfo.newBuilder()
                .setPhone(user.getPhone())
                .setUsername(user.getUsername())
                .setUserType(UserType.valueOf(user.getUserType().name()))
                .build();
    }
}
