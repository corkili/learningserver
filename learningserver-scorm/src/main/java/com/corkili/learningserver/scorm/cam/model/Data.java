package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

public class Data {

    // elements
    private List<Map> mapList; // 1...n

    public Data() {
        mapList = new ArrayList<>();
    }

    public List<Map> getMapList() {
        return mapList;
    }

    public void setMapList(List<Map> mapList) {
        this.mapList = mapList;
    }
}
