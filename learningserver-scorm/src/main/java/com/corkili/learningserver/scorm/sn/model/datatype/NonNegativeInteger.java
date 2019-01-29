package com.corkili.learningserver.scorm.sn.model.datatype;

public class NonNegativeInteger {

    private int value;

    public NonNegativeInteger(int value) {
        setValue(value);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        if (value >= 0) {
            this.value = value;
        } else {
            throw new IllegalArgumentException();
        }
    }
}
