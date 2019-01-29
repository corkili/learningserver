package com.corkili.learningserver.scorm.sn.model.tracking;

import com.corkili.learningserver.scorm.sn.model.datatype.DecimalWithRange;

/**
 * For each attempt on an activity, a learner gets one set of obejctive progress information for
 * each objective associated with the activity.
 */
public class ObjectiveProgressInformation {

    private boolean objectiveProgressStatus4Satisfied;
    private boolean objectiveSatisfiedStatus;

    private boolean objectiveMeasureStatus;
    private final DecimalWithRange objectiveNormalizedMeasure;

    private boolean objectiveProgressStatus4Completion;
    private boolean objectiveCompletionStatus;

    private boolean objectiveCompletionAmountStatus;
    private final DecimalWithRange objectiveCompletionAmount;

    public ObjectiveProgressInformation() {
        objectiveProgressStatus4Satisfied = false;
        objectiveSatisfiedStatus = false;

        objectiveMeasureStatus = false;
        objectiveNormalizedMeasure = new DecimalWithRange(0, -1, 1, 4);

        objectiveProgressStatus4Completion = false;
        objectiveCompletionStatus = false;

        objectiveCompletionAmountStatus = false;
        objectiveCompletionAmount = new DecimalWithRange(0, 0, 1, 4);
    }

    public boolean isObjectiveProgressStatus4Satisfied() {
        return objectiveProgressStatus4Satisfied;
    }

    public void setObjectiveProgressStatus4Satisfied(boolean objectiveProgressStatus4Satisfied) {
        this.objectiveProgressStatus4Satisfied = objectiveProgressStatus4Satisfied;
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

    public boolean isObjectiveProgressStatus4Completion() {
        return objectiveProgressStatus4Completion;
    }

    public void setObjectiveProgressStatus4Completion(boolean objectiveProgressStatus4Completion) {
        this.objectiveProgressStatus4Completion = objectiveProgressStatus4Completion;
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
