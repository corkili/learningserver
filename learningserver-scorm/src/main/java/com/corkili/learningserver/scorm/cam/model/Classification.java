package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Classification {

    private Vocabulary purpose; // 0...1
    private List<TaxonPath> taxonPathList; // 0...n
    private LanguageStrings description; // 0...1
    private List<LanguageStrings> keywordList; // 0...n

    public Classification() {
        taxonPathList = new ArrayList<>();
        keywordList = new ArrayList<>();
    }

    public Vocabulary getPurpose() {
        return purpose;
    }

    public void setPurpose(Vocabulary purpose) {
        this.purpose = purpose;
    }

    public List<TaxonPath> getTaxonPathList() {
        return taxonPathList;
    }

    public void setTaxonPathList(List<TaxonPath> taxonPathList) {
        this.taxonPathList = taxonPathList;
    }

    public LanguageStrings getDescription() {
        return description;
    }

    public void setDescription(LanguageStrings description) {
        this.description = description;
    }

    public List<LanguageStrings> getKeywordList() {
        return keywordList;
    }

    public void setKeywordList(List<LanguageStrings> keywordList) {
        this.keywordList = keywordList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("purpose", purpose)
                .append("taxonPathList", taxonPathList)
                .append("description", description)
                .append("keywordList", keywordList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Classification that = (Classification) o;

        return new EqualsBuilder()
                .append(purpose, that.purpose)
                .append(taxonPathList, that.taxonPathList)
                .append(description, that.description)
                .append(keywordList, that.keywordList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(purpose)
                .append(taxonPathList)
                .append(description)
                .append(keywordList)
                .toHashCode();
    }
}
