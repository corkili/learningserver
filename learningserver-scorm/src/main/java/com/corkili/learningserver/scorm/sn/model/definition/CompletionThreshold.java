package com.corkili.learningserver.scorm.sn.model.definition;

import com.corkili.learningserver.scorm.sn.model.datatype.DecimalWithRange;

/**
 * @see com.corkili.learningserver.scorm.cam.model.CompletionThreshold
 */
public class CompletionThreshold implements DefinitionElementSet {

    private boolean completedByMeasure;
    private final DecimalWithRange minimumProgressMeasure;
    private final DecimalWithRange progressWeight;

    public CompletionThreshold() {
        completedByMeasure = false;
        minimumProgressMeasure = new DecimalWithRange(1, 0, 1, 4);
        progressWeight = new DecimalWithRange(1, 0, 1, 4);
    }

    public boolean isCompletedByMeasure() {
        return completedByMeasure;
    }

    public void setCompletedByMeasure(boolean completedByMeasure) {
        this.completedByMeasure = completedByMeasure;
    }

    public DecimalWithRange getMinimumProgressMeasure() {
        return minimumProgressMeasure;
    }

    public DecimalWithRange getProgressWeight() {
        return progressWeight;
    }
}
