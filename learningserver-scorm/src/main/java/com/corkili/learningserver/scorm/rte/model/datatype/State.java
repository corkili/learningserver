package com.corkili.learningserver.scorm.rte.model.datatype;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class State implements TerminalDataType {

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
    public void set(String value) {
        if (stateTable.contains(value)) {
            this.value = value;
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String get() {
        return this.value;
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
