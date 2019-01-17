package com.corkili.learningserver.scorm.cam.model;

public class Identifier {

    private String catalog; // 0...1
    private String entry; // 0...1

    public Identifier() {
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }
}
