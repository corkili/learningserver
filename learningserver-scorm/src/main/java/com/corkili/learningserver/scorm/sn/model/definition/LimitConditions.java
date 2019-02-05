package com.corkili.learningserver.scorm.sn.model.definition;

import java.time.Duration;

import com.corkili.learningserver.scorm.sn.model.datatype.NonNegativeInteger;

/**
 * @see com.corkili.learningserver.scorm.cam.model.LimitConditions
 */
public class LimitConditions implements DefinitionElementSet {

    private boolean attemptControl;
    private final NonNegativeInteger attemptLimit;
    private boolean attemptAbsoluteDurationControl;
    private Duration attemptAbsoluteDurationLimit;

    public LimitConditions() {
        attemptControl = false;
        attemptLimit = new NonNegativeInteger(0);
        attemptAbsoluteDurationControl = false;
        attemptAbsoluteDurationLimit = Duration.ZERO;
    }

    public boolean isAttemptControl() {
        return attemptControl;
    }

    public void setAttemptControl(boolean attemptControl) {
        this.attemptControl = attemptControl;
    }

    public NonNegativeInteger getAttemptLimit() {
        return attemptLimit;
    }

    public boolean isAttemptAbsoluteDurationControl() {
        return attemptAbsoluteDurationControl;
    }

    public void setAttemptAbsoluteDurationControl(boolean attemptAbsoluteDurationControl) {
        this.attemptAbsoluteDurationControl = attemptAbsoluteDurationControl;
    }

    public Duration getAttemptAbsoluteDurationLimit() {
        return attemptAbsoluteDurationLimit;
    }

    public void setAttemptAbsoluteDurationLimit(Duration attemptAbsoluteDurationLimit) {
        this.attemptAbsoluteDurationLimit = attemptAbsoluteDurationLimit;
    }
}
