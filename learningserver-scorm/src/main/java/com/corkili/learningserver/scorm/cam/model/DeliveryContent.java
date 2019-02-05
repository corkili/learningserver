package com.corkili.learningserver.scorm.cam.model;

public class DeliveryContent {

    private final String basePath;

    private final String entry;

    private final Content content;

    public DeliveryContent(String basePath, String entry, Content content) {
        this.basePath = basePath;
        this.entry = entry;
        this.content = content;
    }

    public String getBasePath() {
        return basePath;
    }

    public String getEntry() {
        return entry;
    }

    public Content getContent() {
        return content;
    }
}
