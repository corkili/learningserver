package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.Real7WithRange;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;

public class ProgressMeasure implements TerminalDataType {

    private Real7WithRange progressMeasure;

    public ProgressMeasure() {
        this.progressMeasure = new Real7WithRange(0, 1);
    }

    @Override
    public void set(String value) {
        this.progressMeasure.set(value);
    }

    @Override
    public String get() {
        return this.progressMeasure.get();
    }

    public Real7WithRange getProgressMeasure() {
        return progressMeasure;
    }

    public void setProgressMeasure(Real7WithRange progressMeasure) {
        this.progressMeasure = progressMeasure;
    }
}
