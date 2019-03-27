package com.corkili.learningserver.scorm.cam.model;

public class DeliveryContent {

    private final String itemId;

    private final String basePath;

    private final String entry;

    private final String parameters;

    private final Content content;

    public DeliveryContent(String itemId, String basePath, String entry, String parameters, Content content) {
        this.itemId = itemId;
        this.basePath = basePath;
        this.entry = entry;
        this.parameters = parameters;
        this.content = content;
    }

    public String getItemId() {
        return itemId;
    }

    public String getBasePath() {
        return basePath;
    }

    public String getEntry() {
        return entry;
    }

    public String getParameters() {
        return parameters;
    }

    public Content getContent() {
        return content;
    }
}
