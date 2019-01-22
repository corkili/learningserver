package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class General {

    private List<Identifier> identifierList; // 0...n
    private LanguageStrings title; // 0...1
    private List<String> languageList; //0...n
    private List<LanguageStrings> descriptionList; // 0...n
    private List<LanguageStrings> keywordList; // 0...n
    private List<LanguageStrings> coverageList; // 0...n
    private Vocabulary structure; // 0...1
    private Vocabulary aggregationLevel; // 0...1

    public General() {
        identifierList = new ArrayList<>();
        languageList = new ArrayList<>();
        descriptionList = new ArrayList<>();
        keywordList = new ArrayList<>();
        coverageList = new ArrayList<>();
    }

    public List<Identifier> getIdentifierList() {
        return identifierList;
    }

    public void setIdentifierList(List<Identifier> identifierList) {
        this.identifierList = identifierList;
    }

    public LanguageStrings getTitle() {
        return title;
    }

    public void setTitle(LanguageStrings title) {
        this.title = title;
    }

    public List<String> getLanguageList() {
        return languageList;
    }

    public void setLanguageList(List<String> languageList) {
        this.languageList = languageList;
    }

    public List<LanguageStrings> getDescriptionList() {
        return descriptionList;
    }

    public void setDescriptionList(List<LanguageStrings> descriptionList) {
        this.descriptionList = descriptionList;
    }

    public List<LanguageStrings> getKeywordList() {
        return keywordList;
    }

    public void setKeywordList(List<LanguageStrings> keywordList) {
        this.keywordList = keywordList;
    }

    public List<LanguageStrings> getCoverageList() {
        return coverageList;
    }

    public void setCoverageList(List<LanguageStrings> coverageList) {
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
                .append("identifierList", identifierList)
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
                .append(identifierList, general.identifierList)
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
                .append(identifierList)
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
