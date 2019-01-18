package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.StringUtils;

import com.corkili.learningserver.scorm.cam.load.ModelUtils;

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
        updateContent();
        updateContentPackageType();
    }

    public Content getContent() {
        updateContent();
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public ContentPackageType getContentPackageType() {
        updateContentPackageType();
        return contentPackageType;
    }

    public void setContentPackageType(ContentPackageType contentPackageType) {
        this.contentPackageType = contentPackageType;
    }

    private void updateContent() {
        // 根据manifest自动解析
        if (manifest == null) {
            return;
        }
        final Content content = new Content();
        final String manifestXmlBase = ModelUtils.isAnyUriEmpty(manifest.getXmlBase()) ? "" : manifest.getXmlBase().getValue();
        final String resourcesXmlBase = ModelUtils.isAnyUriEmpty(manifest.getResources().getXmlBase()) ?
                "" : manifest.getResources().getXmlBase().getValue();
        manifest.getResources().getResourceList().forEach(resource -> {
            final String resourceXmlBase = ModelUtils.isAnyUriEmpty(resource.getXmlBase()) ? "" : resource.getXmlBase().getValue();
            resource.getFileList().forEach(file -> {
                if (StringUtils.isNotBlank(file.getHref())) {
                    content.getPhysicalFilePathList().add(manifestXmlBase + resourcesXmlBase + resourceXmlBase + file.getHref());
                }
            });
        });
        this.content = content;
    }

    private void updateContentPackageType() {
        // 根据manifest.organizations是否为空，设置type
        if (manifest.getOrganizations().getOrganizationList().isEmpty()) {
            contentPackageType = ContentPackageType.RESOURCE_CONTENT_PACKAGE;
        } else {
            contentPackageType = ContentPackageType.CONTENT_AGGREGATION_CONTENT_PACKAGE;
        }
    }

    public enum ContentPackageType {
        CONTENT_AGGREGATION_CONTENT_PACKAGE,
        RESOURCE_CONTENT_PACKAGE;
    }
}
