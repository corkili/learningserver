package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.common.CommonUtils;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;

public class Language implements TerminalDataType {

    private String language;

    public Language() {
    }

    @Override
    public void set(String value) {
        if (!CommonUtils.isLegalLanguage(value) || "".equals(value)) {
            throw new IllegalArgumentException();
        }
        this.language = value;
    }

    @Override
    public String get() {
        return language;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
