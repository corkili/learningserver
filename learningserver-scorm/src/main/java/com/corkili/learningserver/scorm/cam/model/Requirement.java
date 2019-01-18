package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Requirement {

    private List<OrComposite> orCompositeList; // 0...n

    public Requirement() {
        orCompositeList = new ArrayList<>();
    }

    public List<OrComposite> getOrCompositeList() {
        return orCompositeList;
    }

    public void setOrCompositeList(List<OrComposite> orCompositeList) {
        this.orCompositeList = orCompositeList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("orCompositeList", orCompositeList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Requirement that = (Requirement) o;

        return new EqualsBuilder()
                .append(orCompositeList, that.orCompositeList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(orCompositeList)
                .toHashCode();
    }
}
