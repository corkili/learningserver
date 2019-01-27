package com.corkili.learningserver.scorm.rte.model.handler;

import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class WriteOnlyHandler implements GetHandler {

    @Override
    public ScormResult handle(Object context) {
        return new ScormResult("", ScormError.E_405);
    }
}
