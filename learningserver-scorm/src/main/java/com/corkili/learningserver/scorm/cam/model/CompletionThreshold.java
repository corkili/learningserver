package com.corkili.learningserver.scorm.cam.model;

import com.corkili.learningserver.scorm.cam.model.datatype.Decimal;

public class CompletionThreshold {

    // attributes
    private boolean completedByMeasure; // O false
    private Decimal minProgressMeasure; // O 1.0 [0.0000,1.0000]
    private Decimal progressWeight; // O 1.0 [0.0000,1.0000]

    public CompletionThreshold() {
        completedByMeasure = false;
        minProgressMeasure = new Decimal("1.0", 4);
        progressWeight = new Decimal("1.0", 4);
    }

    public boolean isCompletedByMeasure() {
        return completedByMeasure;
    }

    public void setCompletedByMeasure(boolean completedByMeasure) {
        this.completedByMeasure = completedByMeasure;
    }

    public Decimal getMinProgressMeasure() {
        return minProgressMeasure;
    }

    public void setMinProgressMeasure(Decimal minProgressMeasure) {
        this.minProgressMeasure = minProgressMeasure;
    }

    public Decimal getProgressWeight() {
        return progressWeight;
    }

    public void setProgressWeight(Decimal progressWeight) {
        this.progressWeight = progressWeight;
    }
}
