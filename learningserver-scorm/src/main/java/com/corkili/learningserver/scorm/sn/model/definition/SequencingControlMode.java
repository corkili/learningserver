package com.corkili.learningserver.scorm.sn.model.definition;

/**
 * @see com.corkili.learningserver.scorm.cam.model.ControlMode
 */
public class SequencingControlMode implements DefinitionElementSet {

    private boolean sequencingControlChoice;
    private boolean sequencingControlChoiceExit;
    private boolean sequencingControlFlow;
    private boolean sequencingControlForwardOnly;
    private boolean useCurrentAttemptObjectiveInformation;
    private boolean useCurrentAttemptProgressInformation;

    public SequencingControlMode() {
        sequencingControlChoice = true;
        sequencingControlChoiceExit = true;
        sequencingControlFlow = false;
        sequencingControlForwardOnly = false;
        useCurrentAttemptObjectiveInformation = true;
        useCurrentAttemptProgressInformation = true;
    }

    public boolean isSequencingControlChoice() {
        return sequencingControlChoice;
    }

    public void setSequencingControlChoice(boolean sequencingControlChoice) {
        this.sequencingControlChoice = sequencingControlChoice;
    }

    public boolean isSequencingControlChoiceExit() {
        return sequencingControlChoiceExit;
    }

    public void setSequencingControlChoiceExit(boolean sequencingControlChoiceExit) {
        this.sequencingControlChoiceExit = sequencingControlChoiceExit;
    }

    public boolean isSequencingControlFlow() {
        return sequencingControlFlow;
    }

    public void setSequencingControlFlow(boolean sequencingControlFlow) {
        this.sequencingControlFlow = sequencingControlFlow;
    }

    public boolean isSequencingControlForwardOnly() {
        return sequencingControlForwardOnly;
    }

    public void setSequencingControlForwardOnly(boolean sequencingControlForwardOnly) {
        this.sequencingControlForwardOnly = sequencingControlForwardOnly;
    }

    public boolean isUseCurrentAttemptObjectiveInformation() {
        return useCurrentAttemptObjectiveInformation;
    }

    public void setUseCurrentAttemptObjectiveInformation(boolean useCurrentAttemptObjectiveInformation) {
        this.useCurrentAttemptObjectiveInformation = useCurrentAttemptObjectiveInformation;
    }

    public boolean isUseCurrentAttemptProgressInformation() {
        return useCurrentAttemptProgressInformation;
    }

    public void setUseCurrentAttemptProgressInformation(boolean useCurrentAttemptProgressInformation) {
        this.useCurrentAttemptProgressInformation = useCurrentAttemptProgressInformation;
    }
}
