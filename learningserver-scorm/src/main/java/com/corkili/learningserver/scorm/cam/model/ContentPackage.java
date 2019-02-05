package com.corkili.learningserver.scorm.cam.model;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.corkili.learningserver.scorm.cam.load.ModelUtils;
import com.corkili.learningserver.scorm.cam.model.util.CPUtils;

/**
 * A package represents a unit of learning.
 */
public class ContentPackage {

    private final String basePath;

    private Manifest manifest; // 1...1

    public ContentPackage(String basePath) {
        this.basePath = basePath;
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

    public DeliveryContent getDeliveryContent(String itemID) {
        Item item  = CPUtils.findItemByIdentifier(this, itemID);
        if (item == null || !item.getItemList().isEmpty() ||  StringUtils.isBlank(item.getIdentifierref())) {
            return null;
        }
        Resource resourceRefByItem = CPUtils.findResource(this, item.getIdentifierref());
        if (resourceRefByItem == null) {
            return null;
        }
        List<Resource> resources = new LinkedList<>();
        resources.add(resourceRefByItem);
        CPUtils.findResource(this, resourceRefByItem, resources);
        Set<String> resourceIDSet = new HashSet<>();
        List<Resource> resourceList = new LinkedList<>();
        for (Resource res : resources) {
            if (!resourceIDSet.contains(res.getIdentifier().getValue())) {
                resourceIDSet.add(res.getIdentifier().getValue());
                resourceList.add(res);
            }
        }
        final Content content = new Content();
        final String manifestXmlBase = !ModelUtils.isAnyUriEmpty(manifest.getXmlBase()) ? manifest.getXmlBase().getValue() : "";
        final String resourcesXmlBase = !ModelUtils.isAnyUriEmpty(manifest.getResources().getXmlBase()) ?
                manifest.getResources().getXmlBase().getValue() : "";
        resourceList.forEach(resource -> {
            final String resourceXmlBase = !ModelUtils.isAnyUriEmpty(resource.getXmlBase()) ? resource.getXmlBase().getValue() : "";
            resource.getFileList().forEach(file -> {
                if (StringUtils.isNotBlank(file.getHref())) {
                    content.getPhysicalFilePathList().add(manifestXmlBase + resourcesXmlBase + resourceXmlBase + file.getHref());
                }
            });
        });
        String entry = manifestXmlBase + resourcesXmlBase +
                (!ModelUtils.isAnyUriEmpty(resourceRefByItem.getXmlBase()) ? resourceRefByItem.getXmlBase().getValue() : "")
                + resourceRefByItem.getHref();
        return new DeliveryContent(basePath, entry, content);
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

    public String getBasePath() {
        return basePath;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("basePath", basePath)
                .append("manifest", manifest)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ContentPackage that = (ContentPackage) o;

        return new EqualsBuilder()
                .append(basePath, that.basePath)
                .append(manifest, that.manifest)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(basePath)
                .append(manifest)
                .toHashCode();
    }

    public enum ContentPackageType {
        CONTENT_AGGREGATION_CONTENT_PACKAGE,
        RESOURCE_CONTENT_PACKAGE;
    }
}
