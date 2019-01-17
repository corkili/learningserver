package com.corkili.learningserver.scorm.cam.model;

public class LanguageString {

    private String language; // O
    private String value;

    public LanguageString(String language, String value) {
        this.language = language;
        this.value = value;
    }

    public LanguageString(String value) {
        this.value = value;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}
