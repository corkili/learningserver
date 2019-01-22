package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class AdlseqObjectives {

    // elements
    private List<AdlseqObjective> objectiveList; // 1...n

    public AdlseqObjectives() {
        objectiveList = new ArrayList<>();
    }

    public List<AdlseqObjective> getObjectiveList() {
        return objectiveList;
    }

    public void setObjectiveList(List<AdlseqObjective> objectiveList) {
        this.objectiveList = objectiveList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("objectiveList", objectiveList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AdlseqObjectives that = (AdlseqObjectives) o;

        return new EqualsBuilder()
                .append(objectiveList, that.objectiveList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(objectiveList)
                .toHashCode();
    }
}
