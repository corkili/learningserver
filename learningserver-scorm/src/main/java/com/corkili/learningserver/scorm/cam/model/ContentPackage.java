package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    public ContentPackageType getContentPackageType() {
        updateContentPackageType();
        return contentPackageType;
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
        if (manifest == null) {
            return;
        }
        // 根据manifest.organizations是否为空，设置type
        if (manifest.getOrganizations().getOrganizationList().isEmpty()) {
            contentPackageType = ContentPackageType.RESOURCE_CONTENT_PACKAGE;
        } else {
            contentPackageType = ContentPackageType.CONTENT_AGGREGATION_CONTENT_PACKAGE;
        }
    }

    @Override
    public String toString() {
        updateContent();
        updateContentPackageType();
        return new ToStringBuilder(this)
                .append("manifest", manifest)
                .append("content", content)
                .append("contentPackageType", contentPackageType)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        updateContent();
        updateContentPackageType();
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ContentPackage that = (ContentPackage) o;

        return new EqualsBuilder()
                .append(manifest, that.manifest)
                .append(content, that.content)
                .append(contentPackageType, that.contentPackageType)
                .isEquals();
    }

    @Override
    public int hashCode() {
        updateContent();
        updateContentPackageType();
        return new HashCodeBuilder(17, 37)
                .append(manifest)
                .append(content)
                .append(contentPackageType)
                .toHashCode();
    }

    public enum ContentPackageType {
        CONTENT_AGGREGATION_CONTENT_PACKAGE,
        RESOURCE_CONTENT_PACKAGE;
    }
}
