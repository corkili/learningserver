package com.corkili.learningserver.scorm.cam.model;

import com.corkili.learningserver.scorm.cam.model.datatype.NonNegativeInteger;

public class LimitConditions {

    // attributes
    private NonNegativeInteger attemptLimit; // O
    private String attemptAbsoluteDurationLimit; // O

    public LimitConditions() {
    }

    public NonNegativeInteger getAttemptLimit() {
        return attemptLimit;
    }

    public void setAttemptLimit(NonNegativeInteger attemptLimit) {
        this.attemptLimit = attemptLimit;
    }

    public String getAttemptAbsoluteDurationLimit() {
        return attemptAbsoluteDurationLimit;
    }

    public void setAttemptAbsoluteDurationLimit(String attemptAbsoluteDurationLimit) {
        this.attemptAbsoluteDurationLimit = attemptAbsoluteDurationLimit;
    }
}
