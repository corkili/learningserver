package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import com.corkili.learningserver.scorm.cam.model.datatype.VCard;

public class Contribute {

    private Vocabulary role; // 0...1
    private List<VCard> entityList; // 0...n
    private DateTime date; // 0...1

    public Contribute() {
        entityList = new ArrayList<>();
    }

    public Vocabulary getRole() {
        return role;
    }

    public void setRole(Vocabulary role) {
        this.role = role;
    }

    public List<VCard> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<VCard> entityList) {
        this.entityList = entityList;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }
}
