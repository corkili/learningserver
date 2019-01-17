package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

public class Requirement {

    private List<OrComposite> orCompositeList; // 0...n

    public Requirement() {
        orCompositeList = new ArrayList<>();
    }

    public List<OrComposite> getOrCompositeList() {
        return orCompositeList;
    }

    public void setOrCompositeList(List<OrComposite> orCompositeList) {
        this.orCompositeList = orCompositeList;
    }
}
