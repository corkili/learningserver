package com.corkili.learningserver.scorm.cam.model;

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
}
