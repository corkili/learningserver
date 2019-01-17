package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

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
}
