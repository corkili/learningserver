package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.corkili.learningserver.scorm.cam.model.datatype.Token;

public class RollupConsiderations {

    // attributes
    private Token requiredForSatisfied; // O always
    private Token requiredForNotSatisfied; // O always
    private Token requiredForCompleted; // O always
    private Token requiredForIncomplete; // O always
    private boolean measureSatisfactionIfActive; // O true

    public RollupConsiderations() {
        requiredForSatisfied = new Token("always");
        requiredForNotSatisfied = new Token("always");
        requiredForCompleted = new Token("always");
        requiredForIncomplete = new Token("always");
        measureSatisfactionIfActive = true;
    }

    public Token getRequiredForSatisfied() {
        return requiredForSatisfied;
    }

    public void setRequiredForSatisfied(Token requiredForSatisfied) {
        this.requiredForSatisfied = requiredForSatisfied;
    }

    public Token getRequiredForNotSatisfied() {
        return requiredForNotSatisfied;
    }

    public void setRequiredForNotSatisfied(Token requiredForNotSatisfied) {
        this.requiredForNotSatisfied = requiredForNotSatisfied;
    }

    public Token getRequiredForCompleted() {
        return requiredForCompleted;
    }

    public void setRequiredForCompleted(Token requiredForCompleted) {
        this.requiredForCompleted = requiredForCompleted;
    }

    public Token getRequiredForIncomplete() {
        return requiredForIncomplete;
    }

    public void setRequiredForIncomplete(Token requiredForIncomplete) {
        this.requiredForIncomplete = requiredForIncomplete;
    }

    public boolean isMeasureSatisfactionIfActive() {
        return measureSatisfactionIfActive;
    }

    public void setMeasureSatisfactionIfActive(boolean measureSatisfactionIfActive) {
        this.measureSatisfactionIfActive = measureSatisfactionIfActive;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("requiredForSatisfied", requiredForSatisfied)
                .append("requiredForNotSatisfied", requiredForNotSatisfied)
                .append("requiredForCompleted", requiredForCompleted)
                .append("requiredForIncomplete", requiredForIncomplete)
                .append("measureSatisfactionIfActive", measureSatisfactionIfActive)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RollupConsiderations that = (RollupConsiderations) o;

        return new EqualsBuilder()
                .append(measureSatisfactionIfActive, that.measureSatisfactionIfActive)
                .append(requiredForSatisfied, that.requiredForSatisfied)
                .append(requiredForNotSatisfied, that.requiredForNotSatisfied)
                .append(requiredForCompleted, that.requiredForCompleted)
                .append(requiredForIncomplete, that.requiredForIncomplete)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(requiredForSatisfied)
                .append(requiredForNotSatisfied)
                .append(requiredForCompleted)
                .append(requiredForIncomplete)
                .append(measureSatisfactionIfActive)
                .toHashCode();
    }
}
