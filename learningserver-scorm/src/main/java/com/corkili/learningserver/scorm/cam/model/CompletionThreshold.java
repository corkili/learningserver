package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.corkili.learningserver.scorm.cam.model.datatype.Decimal;

public class CompletionThreshold {

    // attributes
    private boolean completedByMeasure; // O false
    private Decimal minProgressMeasure; // O 1.0 [0.0000,1.0000]
    private Decimal progressWeight; // O 1.0 [0.0000,1.0000]

    public CompletionThreshold() {
        completedByMeasure = false;
        minProgressMeasure = new Decimal("1.0", 4);
        progressWeight = new Decimal("1.0", 4);
    }

    public boolean isCompletedByMeasure() {
        return completedByMeasure;
    }

    public void setCompletedByMeasure(boolean completedByMeasure) {
        this.completedByMeasure = completedByMeasure;
    }

    public Decimal getMinProgressMeasure() {
        return minProgressMeasure;
    }

    public void setMinProgressMeasure(Decimal minProgressMeasure) {
        this.minProgressMeasure = minProgressMeasure;
    }

    public Decimal getProgressWeight() {
        return progressWeight;
    }

    public void setProgressWeight(Decimal progressWeight) {
        this.progressWeight = progressWeight;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("completedByMeasure", completedByMeasure)
                .append("minProgressMeasure", minProgressMeasure)
                .append("progressWeight", progressWeight)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        CompletionThreshold that = (CompletionThreshold) o;

        return new EqualsBuilder()
                .append(completedByMeasure, that.completedByMeasure)
                .append(minProgressMeasure, that.minProgressMeasure)
                .append(progressWeight, that.progressWeight)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(completedByMeasure)
                .append(minProgressMeasure)
                .append(progressWeight)
                .toHashCode();
    }
}
