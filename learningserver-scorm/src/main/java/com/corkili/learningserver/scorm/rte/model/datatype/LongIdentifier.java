package com.corkili.learningserver.scorm.rte.model.datatype;

public class LongIdentifier implements TerminalDataType {

    private String value;

    public LongIdentifier() {

    }

    public LongIdentifier(String value) {
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
