package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.corkili.learningserver.scorm.cam.model.datatype.IDRef;

/**
 * Organizations: Contains the content structure or organization
 * of the learning resources making up a stand-alone unit or
 * units of instruction. A definition of sequencing intent can
 * be associated with the content structure.
 */
public class Organizations {

    // attributes
    private IDRef defaultOrganizationID; // M

    // elements
    private List<Organization> organizationList; // 0...n

    public Organizations() {
        organizationList = new ArrayList<>();
    }

    public IDRef getDefaultOrganizationID() {
        return defaultOrganizationID;
    }

    public void setDefaultOrganizationID(IDRef defaultOrganizationID) {
        this.defaultOrganizationID = defaultOrganizationID;
    }

    public List<Organization> getOrganizationList() {
        return organizationList;
    }

    public void setOrganizationList(List<Organization> organizationList) {
        this.organizationList = organizationList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("defaultOrganizationID", defaultOrganizationID)
                .append("organizationList", organizationList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Organizations that = (Organizations) o;

        return new EqualsBuilder()
                .append(defaultOrganizationID, that.defaultOrganizationID)
                .append(organizationList, that.organizationList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(defaultOrganizationID)
                .append(organizationList)
                .toHashCode();
    }
}
