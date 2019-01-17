package com.corkili.learningserver.scorm.cam.model;

import com.corkili.learningserver.scorm.cam.model.datatype.VCard;

public class Annotation {

    private VCard entity; // 0...1
    private DateTime date; // 0...1
    private LanguageString description; // 0...1

    public Annotation() {
    }

    public VCard getEntity() {
        return entity;
    }

    public void setEntity(VCard entity) {
        this.entity = entity;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public LanguageString getDescription() {
        return description;
    }

    public void setDescription(LanguageString description) {
        this.description = description;
    }
}
