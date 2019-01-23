package com.corkili.learningserver.scorm.rte.model.datatype;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.corkili.learningserver.scorm.common.CommonUtils;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class State extends AbstractTerminalDataType {

    private String value;
    private Set<String> stateTable;

    public State(String value, Set<String> stateTable) {
        this.stateTable = stateTable;
        set(value);
    }

    public State(String[] stateTable) {
        this.stateTable = new HashSet<>(Arrays.asList(stateTable));
    }

    @Override
    public ScormResult set(String value) {
        ScormResult scormResult = super.handleSet(this, value);
        if (!scormResult.getError().equals(ScormError.E_0)) {
            return scormResult;
        }
        if (stateTable.contains(value)) {
            this.value = value;
            return scormResult;
        } else {
            return new ScormResult("false", ScormError.E_407, CommonUtils.format(
                    "parameter should be one of the following tokens: {}", (Object) stateTable.toArray()));
        }
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

    public Set<String> getStateTable() {
        return stateTable;
    }

    public void setStateTable(Set<String> stateTable) {
        this.stateTable = stateTable;
    }
}
