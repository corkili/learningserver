package com.corkili.learningserver.scorm.rte.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.corkili.learningserver.scorm.common.CommonUtils;
import com.corkili.learningserver.scorm.rte.model.datatype.AbstractTerminalDataType;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class Result extends AbstractTerminalDataType {

    private static final Set<String> STATE_TABLE
            = new HashSet<>(Arrays.asList("correct", "incorrect", "unanticipated", "neutral"));

    private String result;

    @Override
    public ScormResult set(String value) {
        ScormResult scormResult = super.handleSet(this, value);
        if (!scormResult.getError().equals(ScormError.E_0)) {
            return scormResult;
        }
        boolean valid;
        try {
            Double.parseDouble(value);
            valid = true;
        } catch (Exception e) {
            valid = false;
        }
        valid |= STATE_TABLE.contains(value);
        if (valid) {
            this.result = value;
            return new ScormResult("true", ScormError.E_0);
        } else {
            return new ScormResult("false", ScormError.E_406, CommonUtils.format(
                    "parameter should be a decimal or be one of the following tokens: {}",
                    (Object) STATE_TABLE.toArray()));
        }
    }

    @Override
    public ScormResult get() {
        ScormResult scormResult = super.handleGet(this);
        if (!scormResult.getError().equals(ScormError.E_0)) {
            return scormResult;
        }
        return scormResult.setReturnValue(result);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
