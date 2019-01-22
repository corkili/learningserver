package com.corkili.learningserver.scorm.rte.model.datatype;

public class Time implements TerminalDataType {

    private String value;

    public Time() {
    }

    public Time(String value) {
        this.value = value;
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
