package com.corkili.learningserver.scorm.cam.model;

public class Rights {

    private Vocabulary cost; // 0...1
    private Vocabulary copyrightAndOtherRestrictions; // 0...1
    private LanguageString description; // 0...1

    public Rights() {
    }

    public Vocabulary getCost() {
        return cost;
    }

    public void setCost(Vocabulary cost) {
        this.cost = cost;
    }

    public Vocabulary getCopyrightAndOtherRestrictions() {
        return copyrightAndOtherRestrictions;
    }

    public void setCopyrightAndOtherRestrictions(Vocabulary copyrightAndOtherRestrictions) {
        this.copyrightAndOtherRestrictions = copyrightAndOtherRestrictions;
    }

    public LanguageString getDescription() {
        return description;
    }

    public void setDescription(LanguageString description) {
        this.description = description;
    }
}
