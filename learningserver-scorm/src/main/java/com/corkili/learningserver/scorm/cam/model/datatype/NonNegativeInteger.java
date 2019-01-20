package com.corkili.learningserver.scorm.cam.model.datatype;

public class NonNegativeInteger extends XMLDataType {

    private int intValue;

    public NonNegativeInteger(String value) {
        super(value);
        this.intValue = Integer.parseInt(value);
        if (this.intValue < 0) {
            throw new IllegalArgumentException("value should be non-negative integer");
        }
    }
}
