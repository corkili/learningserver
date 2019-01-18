package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class SequencingCollection {

    private List<Sequencing> sequencingList; // 1...n

    public SequencingCollection() {
        sequencingList = new ArrayList<>();
    }

    public List<Sequencing> getSequencingList() {
        return sequencingList;
    }

    public void setSequencingList(List<Sequencing> sequencingList) {
        this.sequencingList = sequencingList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("sequencingList", sequencingList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        SequencingCollection that = (SequencingCollection) o;

        return new EqualsBuilder()
                .append(sequencingList, that.sequencingList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(sequencingList)
                .toHashCode();
    }
}
