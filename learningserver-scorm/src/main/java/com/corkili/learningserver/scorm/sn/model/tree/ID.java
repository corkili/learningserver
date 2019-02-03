package com.corkili.learningserver.scorm.sn.model.tree;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ID {

    private final String identifier;

    private final String lmsContentPackageID;

    private final String lmsLearnerID;

    public ID(String identifier, String lmsContentPackageID, String lmsLearnerID) {
        this.identifier = identifier;
        this.lmsContentPackageID = lmsContentPackageID;
        this.lmsLearnerID = lmsLearnerID;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getLmsContentPackageID() {
        return lmsContentPackageID;
    }

    public String getLmsLearnerID() {
        return lmsLearnerID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ID id = (ID) o;

        return new EqualsBuilder()
                .append(identifier, id.identifier)
                .append(lmsContentPackageID, id.lmsContentPackageID)
                .append(lmsLearnerID, id.lmsLearnerID)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(identifier)
                .append(lmsContentPackageID)
                .append(lmsLearnerID)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("identifier", identifier)
                .append("lmsContentPackageID", lmsContentPackageID)
                .append("lmsLearnerID", lmsLearnerID)
                .toString();
    }
}
