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
public class TopicComment implements BusinessObject {

    public TopicComment() {
        this.imagePaths = new ArrayList<>();
    }

    private Long id;

    private Date createTime;

    private Date updateTime;

    private String content;

    /**
     * Format:
     *   use {!!!} divide each image path.
     */
    private List<String> imagePaths;

    private Long authorId;

    private Long belongTopicId;

    public static TopicComment copyFrom(TopicComment topicComment) {
        TopicComment copyTopicComment = new TopicComment();
        copyTopicComment.id = topicComment.id;
        copyTopicComment.createTime = topicComment.createTime;
        copyTopicComment.updateTime = topicComment.updateTime;
        copyTopicComment.content = topicComment.content;
        copyTopicComment.imagePaths.addAll(topicComment.imagePaths);
        copyTopicComment.authorId = topicComment.authorId;
        copyTopicComment.belongTopicId = topicComment.belongTopicId;
        return copyTopicComment;
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
