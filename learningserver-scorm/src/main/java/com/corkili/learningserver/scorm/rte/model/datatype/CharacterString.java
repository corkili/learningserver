package com.corkili.learningserver.scorm.rte.model.datatype;

public class CharacterString implements TerminalDataType {

    private String value;

    public CharacterString() {
    }

    public CharacterString(String value) {
        this.value = value;
    }

    @Override
    public void set(String value) {
        this.value = value;
    }

    @Override
    public String get() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
