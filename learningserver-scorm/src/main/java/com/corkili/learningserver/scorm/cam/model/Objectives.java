package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Objectives {

    // elements
    private Objective primaryObjective; // 1...1
    private List<Objective> objectiveList; // 0...n

    public Objectives() {
        objectiveList = new ArrayList<>();
    }

    public Objective getPrimaryObjective() {
        return primaryObjective;
    }

    public void setPrimaryObjective(Objective primaryObjective) {
        this.primaryObjective = primaryObjective;
    }

    public List<Objective> getObjectiveList() {
        return objectiveList;
    }

    public void setObjectiveList(List<Objective> objectiveList) {
        this.objectiveList = objectiveList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("primaryObjective", primaryObjective)
                .append("objectiveList", objectiveList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Objectives that = (Objectives) o;

        return new EqualsBuilder()
                .append(primaryObjective, that.primaryObjective)
                .append(objectiveList, that.objectiveList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(primaryObjective)
                .append(objectiveList)
                .toHashCode();
    }
}
