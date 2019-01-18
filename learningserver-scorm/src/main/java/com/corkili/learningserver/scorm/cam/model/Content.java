package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Content {

    private List<String> physicalFilePathList;

    public Content() {
        physicalFilePathList = new ArrayList<>();
    }

    public List<String> getPhysicalFilePathList() {
        return physicalFilePathList;
    }

    public void setPhysicalFilePathList(List<String> physicalFilePathList) {
        this.physicalFilePathList = physicalFilePathList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("physicalFilePathList", physicalFilePathList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Content content = (Content) o;

        return new EqualsBuilder()
                .append(physicalFilePathList, content.physicalFilePathList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(physicalFilePathList)
                .toHashCode();
    }
}
