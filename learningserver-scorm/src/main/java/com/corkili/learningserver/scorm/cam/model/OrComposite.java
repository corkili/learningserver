package com.corkili.learningserver.scorm.cam.model;

public class OrComposite {

    private Vocabulary type; // 0...1
    private Vocabulary name; // 0...1
    private String minimumVersion; // 0...1
    private String maximumVersion; // 0...1

    public OrComposite() {
    }

    public Vocabulary getType() {
        return type;
    }

    public void setType(Vocabulary type) {
        this.type = type;
    }

    public Vocabulary getName() {
        return name;
    }

    public void setName(Vocabulary name) {
        this.name = name;
    }

    public String getMinimumVersion() {
        return minimumVersion;
    }

    public void setMinimumVersion(String minimumVersion) {
        this.minimumVersion = minimumVersion;
    }

    public String getMaximumVersion() {
        return maximumVersion;
    }

    public void setMaximumVersion(String maximumVersion) {
        this.maximumVersion = maximumVersion;
    }
}
