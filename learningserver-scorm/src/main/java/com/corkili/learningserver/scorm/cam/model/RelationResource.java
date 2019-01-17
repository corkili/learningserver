package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

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
}
