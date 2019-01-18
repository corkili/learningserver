package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ConstrainedChoiceConsiderations {

    // attributes
    private boolean preventActivation; // O false
    private boolean constrainChoice; // O false

    public ConstrainedChoiceConsiderations() {
        preventActivation = false;
        constrainChoice = false;
    }

    public boolean isPreventActivation() {
        return preventActivation;
    }

    public void setPreventActivation(boolean preventActivation) {
        this.preventActivation = preventActivation;
    }

    public boolean isConstrainChoice() {
        return constrainChoice;
    }

    public void setConstrainChoice(boolean constrainChoice) {
        this.constrainChoice = constrainChoice;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("preventActivation", preventActivation)
                .append("constrainChoice", constrainChoice)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ConstrainedChoiceConsiderations that = (ConstrainedChoiceConsiderations) o;

        return new EqualsBuilder()
                .append(preventActivation, that.preventActivation)
                .append(constrainChoice, that.constrainChoice)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(preventActivation)
                .append(constrainChoice)
                .toHashCode();
    }
}
