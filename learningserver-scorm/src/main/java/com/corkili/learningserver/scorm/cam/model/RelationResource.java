package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RelationResource {

    private List<Identifier> identifierList; // 0...n
    private List<LanguageString> descriptionList; // 0...n

    public RelationResource() {
        identifierList = new ArrayList<>();
        descriptionList = new ArrayList<>();
    }

    public List<Identifier> getIdentifierList() {
        return identifierList;
    }

    public void setIdentifierList(List<Identifier> identifierList) {
        this.identifierList = identifierList;
    }

    public List<LanguageString> getDescriptionList() {
        return descriptionList;
    }

    public void setDescriptionList(List<LanguageString> descriptionList) {
        this.descriptionList = descriptionList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("identifierList", identifierList)
                .append("descriptionList", descriptionList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        RelationResource that = (RelationResource) o;

        return new EqualsBuilder()
                .append(identifierList, that.identifierList)
                .append(descriptionList, that.descriptionList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(identifierList)
                .append(descriptionList)
                .toHashCode();
    }
}
