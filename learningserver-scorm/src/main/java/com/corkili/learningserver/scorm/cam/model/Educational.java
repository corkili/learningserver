package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

public class Educational {

    private Vocabulary interactivityType; // 0...1
    private List<Vocabulary> learningResourceTypeList; // 0...n
    private Vocabulary interactivityLevel; // 0...1
    private Vocabulary semanticDensity; // 0...1
    private List<Vocabulary> intendedEndUserRoleList; // 0...n
    private List<Vocabulary> contextList; // 0...n
    private List<LanguageString> typicalAgeRangeList; // 0...n
    private Vocabulary difficulty; // 0...1
    private Duration typicalLearningTime; // 0...1
    private List<LanguageString> descriptionList; // 0...n
    private List<String> languageList; // 0...n

    public Educational() {
    }

    public Vocabulary getInteractivityType() {
        return interactivityType;
    }

    public void setInteractivityType(Vocabulary interactivityType) {
        this.interactivityType = interactivityType;
    }

    public List<Vocabulary> getLearningResourceTypeList() {
        return learningResourceTypeList;
    }

    public void setLearningResourceTypeList(List<Vocabulary> learningResourceTypeList) {
        this.learningResourceTypeList = learningResourceTypeList;
    }

    public Vocabulary getInteractivityLevel() {
        return interactivityLevel;
    }

    public void setInteractivityLevel(Vocabulary interactivityLevel) {
        this.interactivityLevel = interactivityLevel;
    }

    public Vocabulary getSemanticDensity() {
        return semanticDensity;
    }

    public void setSemanticDensity(Vocabulary semanticDensity) {
        this.semanticDensity = semanticDensity;
    }

    public List<Vocabulary> getIntendedEndUserRoleList() {
        return intendedEndUserRoleList;
    }

    public void setIntendedEndUserRoleList(List<Vocabulary> intendedEndUserRoleList) {
        this.intendedEndUserRoleList = intendedEndUserRoleList;
    }

    public List<Vocabulary> getContextList() {
        return contextList;
    }

    public void setContextList(List<Vocabulary> contextList) {
        this.contextList = contextList;
    }

    public List<LanguageString> getTypicalAgeRangeList() {
        return typicalAgeRangeList;
    }

    public void setTypicalAgeRangeList(List<LanguageString> typicalAgeRangeList) {
        this.typicalAgeRangeList = typicalAgeRangeList;
    }

    public Vocabulary getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Vocabulary difficulty) {
        this.difficulty = difficulty;
    }

    public Duration getTypicalLearningTime() {
        return typicalLearningTime;
    }

    public void setTypicalLearningTime(Duration typicalLearningTime) {
        this.typicalLearningTime = typicalLearningTime;
    }

    public List<LanguageString> getDescriptionList() {
        return descriptionList;
    }

    public void setDescriptionList(List<LanguageString> descriptionList) {
        this.descriptionList = descriptionList;
    }

    public List<String> getLanguageList() {
        return languageList;
    }

    public void setLanguageList(List<String> languageList) {
        this.languageList = languageList;
    }
}
