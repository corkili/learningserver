package com.corkili.learningserver.scorm.rte.model.datatype;

import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public interface TerminalDataType {

    ScormResult set(String value);

    ScormResult get();

}
