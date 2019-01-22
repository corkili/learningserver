package com.corkili.learningserver.scorm.rte.model.datatype;

public class TimeInterval implements TerminalDataType {

    String value;

    public TimeInterval() {
    }

    public TimeInterval(String value) {
        set(value);
    }

    @Override
    public void set(String value) {
        this.value = value;
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
}
