package com.corkili.learningserver.scorm.rte.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;

public class Result implements TerminalDataType {

    private static final Set<String> STATE_TABLE
            = new HashSet<>(Arrays.asList("correct", "incorrect", "unanticipated", "neutral"));

    private String result;

    @Override
    public void set(String value) {
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
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String get() {
        return result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
