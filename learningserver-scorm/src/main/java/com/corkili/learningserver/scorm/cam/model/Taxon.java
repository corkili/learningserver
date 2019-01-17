package com.corkili.learningserver.scorm.cam.model;

public class Taxon {

    private String id; // 0...1
    private LanguageString entry; // 0...1

    public Taxon() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LanguageString getEntry() {
        return entry;
    }

    public void setEntry(LanguageString entry) {
        this.entry = entry;
    }
}
