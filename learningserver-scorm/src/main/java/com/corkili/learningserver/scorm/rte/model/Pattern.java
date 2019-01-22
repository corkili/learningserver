package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;

public class Pattern implements TerminalDataType {

    private String pattern;

    @Override
    public void set(String value) {
        this.pattern = value;
    }

    @Override
    public String get() {
        return this.pattern;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
