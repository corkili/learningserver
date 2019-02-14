package com.corkili.learningserver.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.corkili.learningserver.common.ServiceUtils;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class TopicReply implements BusinessObject {

    public TopicReply() {
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

    private Long belongCommentId;

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
