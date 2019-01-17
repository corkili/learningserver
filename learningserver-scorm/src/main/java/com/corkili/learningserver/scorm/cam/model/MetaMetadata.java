package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

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
}
