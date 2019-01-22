package com.corkili.learningserver.scorm.rte.model.datatype;

public class Int implements TerminalDataType{

    private int value;

    public Int() {
    }

    public Int(int value) {
        this.value = value;
    }

    @Override
    public void set(String value) {
        this.value = Integer.parseInt(value);
    }

    @Override
    public String get() {
        return String.valueOf(value);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
