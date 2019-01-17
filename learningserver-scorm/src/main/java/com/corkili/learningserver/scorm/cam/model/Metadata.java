package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

public class Metadata {

    private List<LOM> lomList; // 0...n
    private List<String> locationList; // 0...n

    public Metadata() {
        lomList = new ArrayList<>();
        locationList = new ArrayList<>();
    }

    public List<LOM> getLomList() {
        return lomList;
    }

    public void setLomList(List<LOM> lomList) {
        this.lomList = lomList;
    }

    public List<String> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<String> locationList) {
        this.locationList = locationList;
    }
}
