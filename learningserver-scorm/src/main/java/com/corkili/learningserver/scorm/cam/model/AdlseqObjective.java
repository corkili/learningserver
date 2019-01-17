package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import com.corkili.learningserver.scorm.cam.model.datatype.AnyURI;

public class AdlseqObjective {

    // attributes
    private AnyURI objectiveID; // M

    // elements
    private List<AdlseqMapInfo> mapInfoList; // 1...n

    public AdlseqObjective() {
        mapInfoList = new ArrayList<>();
    }

    public AnyURI getObjectiveID() {
        return objectiveID;
    }

    public void setObjectiveID(AnyURI objectiveID) {
        this.objectiveID = objectiveID;
    }

    public List<AdlseqMapInfo> getMapInfoList() {
        return mapInfoList;
    }

    public void setMapInfoList(List<AdlseqMapInfo> mapInfoList) {
        this.mapInfoList = mapInfoList;
    }
}
