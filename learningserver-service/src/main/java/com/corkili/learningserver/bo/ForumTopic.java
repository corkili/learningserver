package com.corkili.learningserver.bo;

import com.corkili.learningserver.common.ServiceUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ForumTopic implements BusinessObject {

    public ForumTopic() {
        imagePaths = new ArrayList<>();
    }

    private Long id;

    private Date createTime;

    private Date updateTime;

    private String title;

    private String description;

    /**
     * Format:
     *   use {!!!} divide each image path.
     */
    private List<String> imagePaths;

    private Long authorId;

    private Long belongCourseId;

    public static ForumTopic copyFrom(ForumTopic forumTopic) {
        ForumTopic copyForumTopic = new ForumTopic();
        copyForumTopic.id = forumTopic.id;
        copyForumTopic.createTime = forumTopic.createTime;
        copyForumTopic.updateTime = forumTopic.updateTime;
        copyForumTopic.title = forumTopic.title;
        copyForumTopic.description = forumTopic.description;
        copyForumTopic.imagePaths.addAll(forumTopic.imagePaths);
        copyForumTopic.authorId = forumTopic.authorId;
        copyForumTopic.belongCourseId = forumTopic.belongCourseId;
        return copyForumTopic;
    }

    public void setImagePaths(String imagePathsStr) {
        imagePaths = ServiceUtils.string2List(imagePathsStr, Pattern.compile("\\{!!!}"));
    }

    public void addImagePath(String imagePath) {
        if (StringUtils.isNotBlank(imagePath)) {
            imagePaths.add(imagePath);
        }
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public String getImagePathsStr() {
        return ServiceUtils.list2String(imagePaths, "{!!!}");
    }

}
