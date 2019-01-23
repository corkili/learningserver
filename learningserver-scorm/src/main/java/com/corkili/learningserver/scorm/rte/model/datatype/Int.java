package com.corkili.learningserver.scorm.rte.model.datatype;

import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class Int extends AbstractTerminalDataType {

    private int value;

    public Int() {
    }

    public Int(int value) {
        this.value = value;
    }

    @Override
    public ScormResult set(String value) {
        ScormResult scormResult = super.handleSet(this, value);
        if (!scormResult.getError().equals(ScormError.E_0)) {
            return scormResult;
        }
        try {
            this.value = Integer.parseInt(value);
            return scormResult;
        } catch (Exception e) {
            return new ScormResult("false", ScormError.E_406, "parameter should be a integer");
        }
    }

    @Override
    public ScormResult get() {
        ScormResult scormResult = super.handleGet(this);
        if (!scormResult.getError().equals(ScormError.E_0)) {
            return scormResult;
        }
        return scormResult.setReturnValue(String.valueOf(value));
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
