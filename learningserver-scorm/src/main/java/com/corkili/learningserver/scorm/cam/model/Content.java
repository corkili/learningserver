package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

public class Content {

    private List<String> physicalFilePathList;

    public Content() {
        physicalFilePathList = new ArrayList<>();
    }

    public List<String> getPhysicalFilePathList() {
        return physicalFilePathList;
    }

    public void setPhysicalFilePathList(List<String> physicalFilePathList) {
        this.physicalFilePathList = physicalFilePathList;
    }
}
