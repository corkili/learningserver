package com.corkili.learningserver.scorm.cam.model;

public class Duration {

    private String duration; // 0...1
    private LanguageString description; // 0...1

    public Duration() {
    }

    public Duration(String duration) {
        this.duration = duration;
    }

    public Duration(LanguageString description) {
        this.description = description;
    }

    public Duration(String duration, LanguageString description) {
        this.duration = duration;
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public LanguageString getDescription() {
        return description;
    }

    public void setDescription(LanguageString description) {
        this.description = description;
    }
}
