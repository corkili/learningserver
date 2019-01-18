package com.corkili.learningserver.scorm.cam.model;

public class File {

    // attributes
    private String href; // M

    // elements
    private Metadata metadata; // 0...1

    public File(String href) {
        this.href = href;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}
