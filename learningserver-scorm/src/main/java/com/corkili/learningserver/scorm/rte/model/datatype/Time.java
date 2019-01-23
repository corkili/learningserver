package com.corkili.learningserver.scorm.rte.model.datatype;

import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class Time extends AbstractTerminalDataType {

    private String value;

    public Time() {
    }

    public Time(String value) {
        this.value = value;
    }

    @Override
    public ScormResult set(String value) {
        ScormResult scormResult = super.handleSet(this, value);
        if (!scormResult.getError().equals(ScormError.E_0)) {
            return scormResult;
        }
        this.value = value;
        return scormResult;
    }

    @Override
    public ScormResult get() {
        ScormResult scormResult = super.handleGet(this);
        if (!scormResult.getError().equals(ScormError.E_0)) {
            return scormResult;
        }
        return scormResult.setReturnValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
