package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ControlMode {

    // attributes
    private boolean choice; // O true
    private boolean choiceExit; // O true
    private boolean flow; // O false
    private boolean forwardOnly; // O false
    private boolean useCurrentAttemptObjectiveInfo; // O true
    private boolean useCurrentAttemptProgressInfo; // O true

    public ControlMode() {
        choice = true;
        choiceExit = true;
        flow = false;
        forwardOnly = false;
        useCurrentAttemptObjectiveInfo = true;
        useCurrentAttemptProgressInfo = true;
    }

    public boolean isChoice() {
        return choice;
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
    }

    public boolean isChoiceExit() {
        return choiceExit;
    }

    public void setChoiceExit(boolean choiceExit) {
        this.choiceExit = choiceExit;
    }

    public boolean isFlow() {
        return flow;
    }

    public void setFlow(boolean flow) {
        this.flow = flow;
    }

    public boolean isForwardOnly() {
        return forwardOnly;
    }

    public void setForwardOnly(boolean forwardOnly) {
        this.forwardOnly = forwardOnly;
    }

    public boolean isUseCurrentAttemptObjectiveInfo() {
        return useCurrentAttemptObjectiveInfo;
    }

    public void setUseCurrentAttemptObjectiveInfo(boolean useCurrentAttemptObjectiveInfo) {
        this.useCurrentAttemptObjectiveInfo = useCurrentAttemptObjectiveInfo;
    }

    public boolean isUseCurrentAttemptProgressInfo() {
        return useCurrentAttemptProgressInfo;
    }

    public void setUseCurrentAttemptProgressInfo(boolean useCurrentAttemptProgressInfo) {
        this.useCurrentAttemptProgressInfo = useCurrentAttemptProgressInfo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("choice", choice)
                .append("choiceExit", choiceExit)
                .append("flow", flow)
                .append("forwardOnly", forwardOnly)
                .append("useCurrentAttemptObjectiveInfo", useCurrentAttemptObjectiveInfo)
                .append("useCurrentAttemptProgressInfo", useCurrentAttemptProgressInfo)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ControlMode that = (ControlMode) o;

        return new EqualsBuilder()
                .append(choice, that.choice)
                .append(choiceExit, that.choiceExit)
                .append(flow, that.flow)
                .append(forwardOnly, that.forwardOnly)
                .append(useCurrentAttemptObjectiveInfo, that.useCurrentAttemptObjectiveInfo)
                .append(useCurrentAttemptProgressInfo, that.useCurrentAttemptProgressInfo)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(choice)
                .append(choiceExit)
                .append(flow)
                .append(forwardOnly)
                .append(useCurrentAttemptObjectiveInfo)
                .append(useCurrentAttemptProgressInfo)
                .toHashCode();
    }
}
