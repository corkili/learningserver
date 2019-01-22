package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class DeliveryControls {

    // attributes
    private boolean tracked; // O true
    private boolean completionSetByContent; // O false
    private boolean objectiveSetByContent; // O false

    public DeliveryControls() {
        tracked = true;
        completionSetByContent = false;
        objectiveSetByContent = false;
    }

    public boolean isTracked() {
        return tracked;
    }

    public void setTracked(boolean tracked) {
        this.tracked = tracked;
    }

    public boolean isCompletionSetByContent() {
        return completionSetByContent;
    }

    public void setCompletionSetByContent(boolean completionSetByContent) {
        this.completionSetByContent = completionSetByContent;
    }

    public boolean isObjectiveSetByContent() {
        return objectiveSetByContent;
    }

    public void setObjectiveSetByContent(boolean objectiveSetByContent) {
        this.objectiveSetByContent = objectiveSetByContent;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("tracked", tracked)
                .append("completionSetByContent", completionSetByContent)
                .append("objectiveSetByContent", objectiveSetByContent)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DeliveryControls that = (DeliveryControls) o;

        return new EqualsBuilder()
                .append(tracked, that.tracked)
                .append(completionSetByContent, that.completionSetByContent)
                .append(objectiveSetByContent, that.objectiveSetByContent)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(tracked)
                .append(completionSetByContent)
                .append(objectiveSetByContent)
                .toHashCode();
    }
}
