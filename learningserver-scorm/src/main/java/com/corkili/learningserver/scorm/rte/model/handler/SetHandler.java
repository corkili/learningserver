package com.corkili.learningserver.scorm.rte.model.handler;

import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public interface SetHandler {

    ScormResult handle(Object content, String value);

}
