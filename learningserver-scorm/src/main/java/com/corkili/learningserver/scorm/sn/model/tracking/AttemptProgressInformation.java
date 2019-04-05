package com.corkili.learningserver.scorm.sn.model.tracking;

import java.time.Duration;

import com.corkili.learningserver.scorm.sn.model.datatype.DecimalWithRange;

/**
 * For each attempt on a tracked activity, a learner gets one set of attempt progress information
 */
public class AttemptProgressInformation {

    private final Object context;
    private boolean attemptProgressStatus;
    private boolean attemptCompletionStatus;
    private boolean attemptCompletionAmountStatus;
    private final DecimalWithRange attemptCompletionAmount;
    private Duration attemptAbsoluteDuration;
    private Duration attemptExperiencedDuration;

    public AttemptProgressInformation(Object context) {
        this.context = context;
        attemptProgressStatus = false;
        attemptCompletionStatus = false;
        attemptCompletionAmountStatus = false;
        attemptCompletionAmount = new DecimalWithRange(0, 0, 1, 4);
        attemptAbsoluteDuration = Duration.ZERO;
        attemptExperiencedDuration = Duration.ZERO;
    }

    public void reinit() {
        attemptProgressStatus = false;
        attemptCompletionStatus = false;
        attemptCompletionAmountStatus = false;
        attemptCompletionAmount.setValue(0);
        attemptAbsoluteDuration = Duration.ZERO;
        attemptExperiencedDuration = Duration.ZERO;
    }

    public Object getContext() {
        return context;
    }

    public boolean isAttemptProgressStatus() {
        return attemptProgressStatus;
    }

    public void setAttemptProgressStatus(boolean attemptProgressStatus) {
        this.attemptProgressStatus = attemptProgressStatus;
    }

    public boolean isAttemptCompletionStatus() {
        return attemptCompletionStatus;
    }

    public void setAttemptCompletionStatus(boolean attemptCompletionStatus) {
        this.attemptCompletionStatus = attemptCompletionStatus;
    }

    public boolean isAttemptCompletionAmountStatus() {
        return attemptCompletionAmountStatus;
    }

    public void setAttemptCompletionAmountStatus(boolean attemptCompletionAmountStatus) {
        this.attemptCompletionAmountStatus = attemptCompletionAmountStatus;
    }

    public DecimalWithRange getAttemptCompletionAmount() {
        return attemptCompletionAmount;
    }

    public Duration getAttemptAbsoluteDuration() {
        return attemptAbsoluteDuration;
    }

    public void setAttemptAbsoluteDuration(Duration attemptAbsoluteDuration) {
        this.attemptAbsoluteDuration = attemptAbsoluteDuration;
    }

    public Duration getAttemptExperiencedDuration() {
        return attemptExperiencedDuration;
    }

    public void setAttemptExperiencedDuration(Duration attemptExperiencedDuration) {
        this.attemptExperiencedDuration = attemptExperiencedDuration;
    }
}
