package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.Real7WithRange;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;

public class CompletionThreshold implements TerminalDataType {

    private Real7WithRange completionThreshold;

    public CompletionThreshold() {
        completionThreshold = new Real7WithRange(0, 1);
    }

    @Override
    public void set(String value) {
        completionThreshold.set(value);
    }

    @Override
    public String get() {
        return completionThreshold.get();
    }

    public Real7WithRange getCompletionThreshold() {
        return completionThreshold;
    }

    public void setCompletionThreshold(Real7WithRange completionThreshold) {
        this.completionThreshold = completionThreshold;
    }
}
