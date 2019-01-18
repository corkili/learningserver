package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.corkili.learningserver.scorm.cam.model.datatype.NonNegativeInteger;

public class LimitConditions {

    // attributes
    private NonNegativeInteger attemptLimit; // O
    private String attemptAbsoluteDurationLimit; // O

    public LimitConditions() {
    }

    public NonNegativeInteger getAttemptLimit() {
        return attemptLimit;
    }

    public void setAttemptLimit(NonNegativeInteger attemptLimit) {
        this.attemptLimit = attemptLimit;
    }

    public String getAttemptAbsoluteDurationLimit() {
        return attemptAbsoluteDurationLimit;
    }

    public void setAttemptAbsoluteDurationLimit(String attemptAbsoluteDurationLimit) {
        this.attemptAbsoluteDurationLimit = attemptAbsoluteDurationLimit;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("attemptLimit", attemptLimit)
                .append("attemptAbsoluteDurationLimit", attemptAbsoluteDurationLimit)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        LimitConditions that = (LimitConditions) o;

        return new EqualsBuilder()
                .append(attemptLimit, that.attemptLimit)
                .append(attemptAbsoluteDurationLimit, that.attemptAbsoluteDurationLimit)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(attemptLimit)
                .append(attemptAbsoluteDurationLimit)
                .toHashCode();
    }
}
