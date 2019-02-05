package com.corkili.learningserver.scorm.sn.model.definition;

import com.corkili.learningserver.scorm.sn.model.datatype.Vocabulary;

/**
 * @see com.corkili.learningserver.scorm.cam.model.RollupConsiderations
 */
public class RollupConsiderationControls implements DefinitionElementSet {

    private boolean measureSatisfactionIfActive;
    private final Vocabulary requiredForSatisfied;
    private final Vocabulary requiredForNotSatisfied;
    private final Vocabulary requiredForCompleted;
    private final Vocabulary requiredForIncomplete;

    public RollupConsiderationControls() {
        measureSatisfactionIfActive = true;
        requiredForSatisfied = new Vocabulary("always",
                "always", "ifNotSuspended", "ifAttempted", "ifNotSkipped");
        requiredForNotSatisfied = new Vocabulary("always",
                "always", "ifNotSuspended", "ifAttempted", "ifNotSkipped");
        requiredForCompleted = new Vocabulary("always",
                "always", "ifNotSuspended", "ifAttempted", "ifNotSkipped");
        requiredForIncomplete = new Vocabulary("always",
                "always", "ifNotSuspended", "ifAttempted", "ifNotSkipped");
    }

    public boolean isMeasureSatisfactionIfActive() {
        return measureSatisfactionIfActive;
    }

    public void setMeasureSatisfactionIfActive(boolean measureSatisfactionIfActive) {
        this.measureSatisfactionIfActive = measureSatisfactionIfActive;
    }

    public Vocabulary getRequiredForSatisfied() {
        return requiredForSatisfied;
    }

    public Vocabulary getRequiredForNotSatisfied() {
        return requiredForNotSatisfied;
    }

    public Vocabulary getRequiredForCompleted() {
        return requiredForCompleted;
    }

    public Vocabulary getRequiredForIncomplete() {
        return requiredForIncomplete;
    }
}
