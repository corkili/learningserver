package com.corkili.learningserver.scorm.cam.model;

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
}
