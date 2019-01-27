package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.Real7WithRange;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.handler.ReadOnlyHandler;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class CompletionThreshold implements TerminalDataType {

    private Real7WithRange completionThreshold;

    public CompletionThreshold() {
        completionThreshold = new Real7WithRange(0, 1);
        registerHandler();
    }

    private void registerHandler() {
        completionThreshold.registerGetHandler(context -> {
            Real7WithRange r = (Real7WithRange) context;
            if (r.getValue() == null) {
                return new ScormResult("", ScormError.E_403);
            }
            return new ScormResult("", ScormError.E_0);
        }).registerSetHandler(new ReadOnlyHandler());
    }

    @Override
    public ScormResult set(String value) {
        return completionThreshold.set(value);
    }

    @Override
    public ScormResult get() {
        return completionThreshold.get();
    }

    public Real7WithRange getCompletionThreshold() {
        return completionThreshold;
    }

    public void setCompletionThreshold(Real7WithRange completionThreshold) {
        this.completionThreshold = completionThreshold;
    }
}
