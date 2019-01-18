package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class General {

    private List<Identifier> identifier; // 0...n
    private LanguageString title; // 0...1
    private List<String> languageList; //0...n
    private List<LanguageString> descriptionList; // 0...n
    private List<LanguageString> keywordList; // 0...n
    private List<LanguageString> coverageList; // 0...n
    private Vocabulary structure; // 0...1
    private Vocabulary aggregationLevel; // 0...1

    public General() {
        identifier = new ArrayList<>();
        languageList = new ArrayList<>();
        descriptionList = new ArrayList<>();
        keywordList = new ArrayList<>();
        coverageList = new ArrayList<>();
    }

    public List<Identifier> getIdentifier() {
        return identifier;
    }

    public void setIdentifier(List<Identifier> identifier) {
        this.identifier = identifier;
    }

    public LanguageString getTitle() {
        return title;
    }

    public void setTitle(LanguageString title) {
        this.title = title;
    }

    public List<String> getLanguageList() {
        return languageList;
    }

    public void setLanguageList(List<String> languageList) {
        this.languageList = languageList;
    }

    public List<LanguageString> getDescriptionList() {
        return descriptionList;
    }

    public void setDescriptionList(List<LanguageString> descriptionList) {
        this.descriptionList = descriptionList;
    }

    public List<LanguageString> getKeywordList() {
        return keywordList;
    }

    public void setKeywordList(List<LanguageString> keywordList) {
        this.keywordList = keywordList;
    }

    public List<LanguageString> getCoverageList() {
        return coverageList;
    }

    public void setCoverageList(List<LanguageString> coverageList) {
        this.coverageList = coverageList;
    }

    public Vocabulary getStructure() {
        return structure;
    }

    public void setStructure(Vocabulary structure) {
        this.structure = structure;
    }

    public Vocabulary getAggregationLevel() {
        return aggregationLevel;
    }

    public void setAggregationLevel(Vocabulary aggregationLevel) {
        this.aggregationLevel = aggregationLevel;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("identifier", identifier)
                .append("title", title)
                .append("languageList", languageList)
                .append("descriptionList", descriptionList)
                .append("keywordList", keywordList)
                .append("coverageList", coverageList)
                .append("structure", structure)
                .append("aggregationLevel", aggregationLevel)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        General general = (General) o;

        return new EqualsBuilder()
                .append(identifier, general.identifier)
                .append(title, general.title)
                .append(languageList, general.languageList)
                .append(descriptionList, general.descriptionList)
                .append(keywordList, general.keywordList)
                .append(coverageList, general.coverageList)
                .append(structure, general.structure)
                .append(aggregationLevel, general.aggregationLevel)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(identifier)
                .append(title)
                .append(languageList)
                .append(descriptionList)
                .append(keywordList)
                .append(coverageList)
                .append(structure)
                .append(aggregationLevel)
                .toHashCode();
    }
}
