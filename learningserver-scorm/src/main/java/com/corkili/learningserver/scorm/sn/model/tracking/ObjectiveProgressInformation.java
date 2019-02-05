package com.corkili.learningserver.scorm.sn.model.tracking;

import com.corkili.learningserver.scorm.sn.model.datatype.DecimalWithRange;

/**
 * For each attempt on an activity, a learner gets one set of objective progress information for
 * each objective associated with the activity.
 */
public class ObjectiveProgressInformation {

    private boolean objectiveProgressStatus;
    private boolean objectiveSatisfiedStatus;

    private boolean objectiveMeasureStatus;
    private final DecimalWithRange objectiveNormalizedMeasure;

    private boolean objectiveProgress4CompletionStatus;
    private boolean objectiveCompletionStatus;

    private boolean objectiveCompletionAmountStatus;
    private final DecimalWithRange objectiveCompletionAmount;

    public ObjectiveProgressInformation() {
        objectiveProgressStatus = false;
        objectiveSatisfiedStatus = false;

        objectiveMeasureStatus = false;
        objectiveNormalizedMeasure = new DecimalWithRange(0, -1, 1, 4);

        objectiveCompletionStatus = false;

        objectiveCompletionAmountStatus = false;
        objectiveCompletionAmount = new DecimalWithRange(0, 0, 1, 4);
    }

    public void reinit() {
        objectiveProgressStatus = false;
        objectiveSatisfiedStatus = false;

        objectiveMeasureStatus = false;
        objectiveNormalizedMeasure.setValue(0);

        objectiveCompletionStatus = false;

        objectiveCompletionAmountStatus = false;
        objectiveCompletionAmount.setValue(0);
    }

    public boolean isObjectiveProgressStatus() {
        return objectiveProgressStatus;
    }

    public void setObjectiveProgressStatus(boolean objectiveProgressStatus) {
        this.objectiveProgressStatus = objectiveProgressStatus;
    }

    public boolean isObjectiveSatisfiedStatus() {
        return objectiveSatisfiedStatus;
    }

    public void setObjectiveSatisfiedStatus(boolean objectiveSatisfiedStatus) {
        this.objectiveSatisfiedStatus = objectiveSatisfiedStatus;
    }

    public boolean isObjectiveMeasureStatus() {
        return objectiveMeasureStatus;
    }

    public void setObjectiveMeasureStatus(boolean objectiveMeasureStatus) {
        this.objectiveMeasureStatus = objectiveMeasureStatus;
    }

    public DecimalWithRange getObjectiveNormalizedMeasure() {
        return objectiveNormalizedMeasure;
    }

    public boolean isObjectiveProgress4CompletionStatus() {
        return objectiveProgress4CompletionStatus;
    }

    public ObjectiveProgressInformation setObjectiveProgress4CompletionStatus(boolean objectiveProgress4CompletionStatus) {
        this.objectiveProgress4CompletionStatus = objectiveProgress4CompletionStatus;
        return this;
    }

    public boolean isObjectiveCompletionStatus() {
        return objectiveCompletionStatus;
    }

    public void setObjectiveCompletionStatus(boolean objectiveCompletionStatus) {
        this.objectiveCompletionStatus = objectiveCompletionStatus;
    }

    public boolean isObjectiveCompletionAmountStatus() {
        return objectiveCompletionAmountStatus;
    }

    public void setObjectiveCompletionAmountStatus(boolean objectiveCompletionAmountStatus) {
        this.objectiveCompletionAmountStatus = objectiveCompletionAmountStatus;
    }

    public DecimalWithRange getObjectiveCompletionAmount() {
        return objectiveCompletionAmount;
    }
}
