package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Dependency {

    // attributes
    private String identifierref; // M

    public Dependency() {
    }

    public String getIdentifierref() {
        return identifierref;
    }

    public void setIdentifierref(String identifierref) {
        this.identifierref = identifierref;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("identifierref", identifierref)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Dependency that = (Dependency) o;

        return new EqualsBuilder()
                .append(identifierref, that.identifierref)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(identifierref)
                .toHashCode();
    }
}
