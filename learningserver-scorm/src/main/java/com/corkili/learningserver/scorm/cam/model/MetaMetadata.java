package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class MetaMetadata {

    private List<Identifier> identifierList; // 0...n
    private List<Contribute> contribute; // 0...n
    private List<String> metadataSchema; // 0...n
    private String language; // 0...1

    public MetaMetadata() {
        identifierList = new ArrayList<>();
        contribute = new ArrayList<>();
        metadataSchema = new ArrayList<>();
    }

    public List<Identifier> getIdentifierList() {
        return identifierList;
    }

    public void setIdentifierList(List<Identifier> identifierList) {
        this.identifierList = identifierList;
    }

    public List<Contribute> getContribute() {
        return contribute;
    }

    public void setContribute(List<Contribute> contribute) {
        this.contribute = contribute;
    }

    public List<String> getMetadataSchema() {
        return metadataSchema;
    }

    public void setMetadataSchema(List<String> metadataSchema) {
        this.metadataSchema = metadataSchema;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("identifierList", identifierList)
                .append("contribute", contribute)
                .append("metadataSchema", metadataSchema)
                .append("language", language)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        MetaMetadata that = (MetaMetadata) o;

        return new EqualsBuilder()
                .append(identifierList, that.identifierList)
                .append(contribute, that.contribute)
                .append(metadataSchema, that.metadataSchema)
                .append(language, that.language)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(identifierList)
                .append(contribute)
                .append(metadataSchema)
                .append(language)
                .toHashCode();
    }
}
