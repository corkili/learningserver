package com.corkili.learningserver.scorm.cam.model;

/**
 * A package represents a unit of learning.
 */
public class ContentPackage {

    private Manifest manifest; // 1...1
    private Content content;
    private ContentPackageType contentPackageType;

    public ContentPackage() {
    }

    public Manifest getManifest() {
        return manifest;
    }

    public void setManifest(Manifest manifest) {
        this.manifest = manifest;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public ContentPackageType getContentPackageType() {
        return contentPackageType;
    }

    public void setContentPackageType(ContentPackageType contentPackageType) {
        this.contentPackageType = contentPackageType;
    }

    public void updateContent() {
        // TODO: 根据manifest自动解析
    }

    public void updateContentPackageType() {
        // TODO 根据manifest.organizations是否为空，设置type
    }

    public enum ContentPackageType {
        CONTENT_AGGREGATION_CONTENT_PACKAGE,
        RESOURCE_CONTENT_PACKAGE;
    }
}
