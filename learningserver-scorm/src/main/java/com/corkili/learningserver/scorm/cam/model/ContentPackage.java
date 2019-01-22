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

    public ContentPackage() {
    }

    public Manifest getManifest() {
        return manifest;
    }

    public void setManifest(Manifest manifest) {
        this.manifest = manifest;
    }

    public Content getContent() {
        // 根据manifest自动解析
        if (manifest == null || manifest.getResources() == null) {
            return null;
        }
        final Content content = new Content();
        final String manifestXmlBase = !ModelUtils.isAnyUriEmpty(manifest.getXmlBase()) ? manifest.getXmlBase().getValue() : "";
        final String resourcesXmlBase = !ModelUtils.isAnyUriEmpty(manifest.getResources().getXmlBase()) ?
                manifest.getResources().getXmlBase().getValue() : "";
        manifest.getResources().getResourceList().forEach(resource -> {
            final String resourceXmlBase = !ModelUtils.isAnyUriEmpty(resource.getXmlBase()) ? resource.getXmlBase().getValue() : "";
            resource.getFileList().forEach(file -> {
                if (StringUtils.isNotBlank(file.getHref())) {
                    content.getPhysicalFilePathList().add(manifestXmlBase + resourcesXmlBase + resourceXmlBase + file.getHref());
                }
            });
        });
        return content;
    }

    public ContentPackageType getContentPackageType() {
        if (manifest == null || manifest.getOrganizations() == null) {
            return null;
        }
        ContentPackageType contentPackageType;
        // 根据manifest.organizations是否为空，设置type
        if (manifest.getOrganizations().getOrganizationList().isEmpty()) {
            contentPackageType = ContentPackageType.RESOURCE_CONTENT_PACKAGE;
        } else {
            contentPackageType = ContentPackageType.CONTENT_AGGREGATION_CONTENT_PACKAGE;
        }
        return contentPackageType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("manifest", manifest)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ContentPackage that = (ContentPackage) o;

        return new EqualsBuilder()
                .append(manifest, that.manifest)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(manifest)
                .toHashCode();
    }

    public enum ContentPackageType {
        CONTENT_AGGREGATION_CONTENT_PACKAGE,
        RESOURCE_CONTENT_PACKAGE;
    }
}
