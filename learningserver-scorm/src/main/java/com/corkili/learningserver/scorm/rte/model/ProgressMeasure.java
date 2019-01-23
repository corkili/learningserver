package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.Real7WithRange;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class ProgressMeasure implements TerminalDataType {

    private Real7WithRange progressMeasure;

    public ProgressMeasure() {
        this.progressMeasure = new Real7WithRange(0, 1);
        registerHandler();
    }

    private void registerHandler() {
        progressMeasure.registerGetHandler(context -> {
            if (((Real7WithRange) context).getValue() == null) {
                return new ScormResult("", ScormError.E_403);
            }
            return new ScormResult("", ScormError.E_0);
        });
    }

    @Override
    public ScormResult set(String value) {
        return this.progressMeasure.set(value);
    }

    @Override
    public ScormResult get() {
        return this.progressMeasure.get();
    }

    public Real7WithRange getProgressMeasure() {
        return progressMeasure;
    }

    public void setProgressMeasure(Real7WithRange progressMeasure) {
        this.progressMeasure = progressMeasure;
    }
}
