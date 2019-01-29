package com.corkili.learningserver.scorm.sn.model.definition;

/**
 * @see com.corkili.learningserver.scorm.cam.model.ConstrainedChoiceConsiderations
 */
public class ConstrainChoiceControls implements DefinitionElementSet {

    private boolean constrainChoice;
    private boolean preventActivation;

    public ConstrainChoiceControls() {
        constrainChoice = false;
        preventActivation = false;
    }

    public boolean isConstrainChoice() {
        return constrainChoice;
    }

    public void setConstrainChoice(boolean constrainChoice) {
        this.constrainChoice = constrainChoice;
    }

    public boolean isPreventActivation() {
        return preventActivation;
    }

    public void setPreventActivation(boolean preventActivation) {
        this.preventActivation = preventActivation;
    }
}
