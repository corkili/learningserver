package com.corkili.learningserver.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

import com.corkili.learningserver.common.ServiceUtils;

@Getter
@Setter
public class Course {

    public Course() {
        this.imagePaths = new ArrayList<>();
        this.tags = new ArrayList<>();
    }

    private Long id;

    private Date createTime;

    private Date updateTime;

    private String courseName;

    private String description;

    /**
     * Format:
     *   use {!!!} divide each image path.
     */
    private List<String> imagePaths;

    /**
     * format: use ; divide each tag
     *
     *   tag1;tag2;tag3
     */
    private List<String> tags;

    private CourseCategory category;

    private User teacher;

    private Scorm courseware;

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

    public void setTags(String tagsStr) {
        tags = ServiceUtils.string2List(tagsStr, Pattern.compile(";"));
    }

    public void addTag(String tag) {
        if (StringUtils.isNotBlank(tag)) {
            tags.add(tag);
        }
    }

    public List<String> getTags() {
        return tags;
    }

    public String getTagsStr() {
        return ServiceUtils.list2String(tags, ";");
    }

}
