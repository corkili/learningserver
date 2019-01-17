package com.corkili.learningserver.scorm.cam.model;

public class DateTime {

    private String dateTime; // 0...1
    private LanguageString description; // 0...1

    public DateTime() {
    }

    public DateTime(String dateTime, LanguageString description) {
        this.dateTime = dateTime;
        this.description = description;
    }

    public DateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public DateTime(LanguageString description) {
        this.description = description;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public LanguageString getDescription() {
        return description;
    }

    public void setDescription(LanguageString description) {
        this.description = description;
    }
}
