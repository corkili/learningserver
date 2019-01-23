package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.common.CommonUtils;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class Language implements TerminalDataType {

    private String language;

    public Language() {
        language = "";
    }

    @Override
    public ScormResult set(String value) {
        if (!CommonUtils.isLegalLanguage(value) || !"".equals(value)) {
            return new ScormResult("false", ScormError.E_406,
                    "parameter should be empty string or a legal language code");
        }
        this.language = value;
        return new ScormResult("true", ScormError.E_0);
    }

    @Override
    public ScormResult get() {
        return new ScormResult(language, ScormError.E_0);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
