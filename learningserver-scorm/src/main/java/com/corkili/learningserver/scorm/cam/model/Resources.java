package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.corkili.learningserver.scorm.cam.model.datatype.AnyURI;

/**
 * Defines the learning resources bundled in the content package.
 */
public class Resources {

    // attributes
    private AnyURI xmlBase; // O

    // elements
    private List<Resource> resourceList; // 0...n

    public Resources() {
        resourceList = new ArrayList<>();
    }

    public AnyURI getXmlBase() {
        return xmlBase;
    }

    public void setXmlBase(AnyURI xmlBase) {
        this.xmlBase = xmlBase;
    }

    public List<Resource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<Resource> resourceList) {
        this.resourceList = resourceList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("xmlBase", xmlBase)
                .append("resourceList", resourceList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Resources resources = (Resources) o;

        return new EqualsBuilder()
                .append(xmlBase, resources.xmlBase)
                .append(resourceList, resources.resourceList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(xmlBase)
                .append(resourceList)
                .toHashCode();
    }
}
