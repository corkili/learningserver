package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Rights {

    private Vocabulary cost; // 0...1
    private Vocabulary copyrightAndOtherRestrictions; // 0...1
    private LanguageString description; // 0...1

    public Rights() {
    }

    public Vocabulary getCost() {
        return cost;
    }

    public void setCost(Vocabulary cost) {
        this.cost = cost;
    }

    public Vocabulary getCopyrightAndOtherRestrictions() {
        return copyrightAndOtherRestrictions;
    }

    public void setCopyrightAndOtherRestrictions(Vocabulary copyrightAndOtherRestrictions) {
        this.copyrightAndOtherRestrictions = copyrightAndOtherRestrictions;
    }

    public LanguageString getDescription() {
        return description;
    }

    public void setDescription(LanguageString description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("cost", cost)
                .append("copyrightAndOtherRestrictions", copyrightAndOtherRestrictions)
                .append("description", description)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Rights rights = (Rights) o;

        return new EqualsBuilder()
                .append(cost, rights.cost)
                .append(copyrightAndOtherRestrictions, rights.copyrightAndOtherRestrictions)
                .append(description, rights.description)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(cost)
                .append(copyrightAndOtherRestrictions)
                .append(description)
                .toHashCode();
    }
}
