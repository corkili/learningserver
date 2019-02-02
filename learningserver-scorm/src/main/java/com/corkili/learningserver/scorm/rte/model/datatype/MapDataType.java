package com.corkili.learningserver.scorm.rte.model.datatype;

import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public interface MapDataType {

    ScormResult set(String key, String value);

    ScormResult get(String key);

}
