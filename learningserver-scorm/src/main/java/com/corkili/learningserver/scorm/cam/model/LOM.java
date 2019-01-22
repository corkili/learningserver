package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class LOM {

    // elements
    private General general; // 0...1
    private LifeCycle lifeCycle; // 0...1
    private MetaMetadata metaMetadata; // 0...1
    private Technical technical; // 0...1
    private List<Educational> educationalList; // 0...n
    private Rights rights; // 0...1
    private List<Relation> relationList; // 0...n
    private List<Annotation> annotationList; // 0...n
    private List<Classification> classificationList; // 0...n

    public LOM() {
        educationalList = new ArrayList<>();
        relationList = new ArrayList<>();
        annotationList = new ArrayList<>();
        classificationList = new ArrayList<>();
    }

    public General getGeneral() {
        return general;
    }

    public void setGeneral(General general) {
        this.general = general;
    }

    public LifeCycle getLifeCycle() {
        return lifeCycle;
    }

    public void setLifeCycle(LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public MetaMetadata getMetaMetadata() {
        return metaMetadata;
    }

    public void setMetaMetadata(MetaMetadata metaMetadata) {
        this.metaMetadata = metaMetadata;
    }

    public Technical getTechnical() {
        return technical;
    }

    public void setTechnical(Technical technical) {
        this.technical = technical;
    }

    public List<Educational> getEducationalList() {
        return educationalList;
    }

    public void setEducationalList(List<Educational> educationalList) {
        this.educationalList = educationalList;
    }

    public Rights getRights() {
        return rights;
    }

    public void setRights(Rights rights) {
        this.rights = rights;
    }

    public List<Relation> getRelationList() {
        return relationList;
    }

    public void setRelationList(List<Relation> relationList) {
        this.relationList = relationList;
    }

    public List<Annotation> getAnnotationList() {
        return annotationList;
    }

    public void setAnnotationList(List<Annotation> annotationList) {
        this.annotationList = annotationList;
    }

    public List<Classification> getClassificationList() {
        return classificationList;
    }

    public void setClassificationList(List<Classification> classificationList) {
        this.classificationList = classificationList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("general", general)
                .append("lifeCycle", lifeCycle)
                .append("metaMetadata", metaMetadata)
                .append("technical", technical)
                .append("educationalList", educationalList)
                .append("rights", rights)
                .append("relationList", relationList)
                .append("annotationList", annotationList)
                .append("classificationList", classificationList)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        LOM lom = (LOM) o;

        return new EqualsBuilder()
                .append(general, lom.general)
                .append(lifeCycle, lom.lifeCycle)
                .append(metaMetadata, lom.metaMetadata)
                .append(technical, lom.technical)
                .append(educationalList, lom.educationalList)
                .append(rights, lom.rights)
                .append(relationList, lom.relationList)
                .append(annotationList, lom.annotationList)
                .append(classificationList, lom.classificationList)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(general)
                .append(lifeCycle)
                .append(metaMetadata)
                .append(technical)
                .append(educationalList)
                .append(rights)
                .append(relationList)
                .append(annotationList)
                .append(classificationList)
                .toHashCode();
    }
}
