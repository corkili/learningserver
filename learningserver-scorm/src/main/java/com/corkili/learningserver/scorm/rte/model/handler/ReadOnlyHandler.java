package com.corkili.learningserver.scorm.rte.model.handler;

import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class ReadOnlyHandler implements SetHandler {

    @Override
    public ScormResult handle(Object context, String value) {
        return new ScormResult("false", ScormError.E_404);
    }
}
