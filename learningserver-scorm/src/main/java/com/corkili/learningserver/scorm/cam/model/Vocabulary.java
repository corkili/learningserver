package com.corkili.learningserver.scorm.cam.model;

public class Vocabulary {

    private String source; // 1...1
    private String value; // 1...1

    public Vocabulary(String source, String value) {
        this.source = source;
        this.value = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
