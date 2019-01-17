package com.corkili.learningserver.scorm.cam.model.datatype;

public abstract class XMLDataType {

    String value;

    public XMLDataType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
