package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Educational {

    private Vocabulary interactivityType; // 0...1
    private List<Vocabulary> learningResourceTypeList; // 0...n
    private Vocabulary interactivityLevel; // 0...1
    private Vocabulary semanticDensity; // 0...1
    private List<Vocabulary> intendedEndUserRoleList; // 0...n
    private List<Vocabulary> contextList; // 0...n
    private List<LanguageStrings> typicalAgeRangeList; // 0...n
    private Vocabulary difficulty; // 0...1
    private Duration typicalLearningTime; // 0...1
    private List<LanguageStrings> descriptionList; // 0...n
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

    public List<LanguageStrings> getTypicalAgeRangeList() {
        return typicalAgeRangeList;
    }

    public void setTypicalAgeRangeList(List<LanguageStrings> typicalAgeRangeList) {
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

    public List<LanguageStrings> getDescriptionList() {
        return descriptionList;
    }

    public void setDescriptionList(List<LanguageStrings> descriptionList) {
        this.descriptionList = descriptionList;
    }

    public List<String> getLanguageList() {
        return languageList;
    }

    public void setLanguageList(List<String> languageList) {
        this.languageList = languageList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("interactivityType", interactivityType)
                .append("learningResourceTypeList", learningResourceTypeList)
                .append("interactivityLevel", interactivityLevel)
                .append("semanticDensity", semanticDensity)
                .append("intendedEndUserRoleList", intendedEndUserRoleList)
                .append("contextList", contextList)
                .append("typicalAgeRangeList", typicalAgeRangeList)
                .append("difficulty", difficulty)
                .append("typicalLearningTime", typicalLearningTime)
                .append("descriptionList", descriptionList)
                .append("languageList", languageList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Educational that = (Educational) o;

        return new EqualsBuilder()
                .append(interactivityType, that.interactivityType)
                .append(learningResourceTypeList, that.learningResourceTypeList)
                .append(interactivityLevel, that.interactivityLevel)
                .append(semanticDensity, that.semanticDensity)
                .append(intendedEndUserRoleList, that.intendedEndUserRoleList)
                .append(contextList, that.contextList)
                .append(typicalAgeRangeList, that.typicalAgeRangeList)
                .append(difficulty, that.difficulty)
                .append(typicalLearningTime, that.typicalLearningTime)
                .append(descriptionList, that.descriptionList)
                .append(languageList, that.languageList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(interactivityType)
                .append(learningResourceTypeList)
                .append(interactivityLevel)
                .append(semanticDensity)
                .append(intendedEndUserRoleList)
                .append(contextList)
                .append(typicalAgeRangeList)
                .append(difficulty)
                .append(typicalLearningTime)
                .append(descriptionList)
                .append(languageList)
                .toHashCode();
    }
}
