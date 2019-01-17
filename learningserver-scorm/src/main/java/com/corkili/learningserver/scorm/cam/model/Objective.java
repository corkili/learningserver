package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import com.corkili.learningserver.scorm.cam.model.datatype.AnyURI;
import com.corkili.learningserver.scorm.cam.model.datatype.Decimal;

public class Objective {

    // attributes
    private boolean satisfiedByMeasure; // O false
    private AnyURI objectiveID; // O(for primaryObjective) M(for Objective)

    // elements
    private Decimal minNormalizedMeasure; // 0...1 1.0 [-1.0000,1.0000]
    private List<MapInfo> mapInfoList; // 0...n

    public Objective() {
        satisfiedByMeasure = false;
        minNormalizedMeasure = new Decimal("1.0", 4);
        mapInfoList = new ArrayList<>();
    }

    public boolean isSatisfiedByMeasure() {
        return satisfiedByMeasure;
    }

    public void setSatisfiedByMeasure(boolean satisfiedByMeasure) {
        this.satisfiedByMeasure = satisfiedByMeasure;
    }

    public AnyURI getObjectiveID() {
        return objectiveID;
    }

    public void setObjectiveID(AnyURI objectiveID) {
        this.objectiveID = objectiveID;
    }

    public Decimal getMinNormalizedMeasure() {
        return minNormalizedMeasure;
    }

    public void setMinNormalizedMeasure(Decimal minNormalizedMeasure) {
        this.minNormalizedMeasure = minNormalizedMeasure;
    }

    public List<MapInfo> getMapInfoList() {
        return mapInfoList;
    }

    public void setMapInfoList(List<MapInfo> mapInfoList) {
        this.mapInfoList = mapInfoList;
    }
}
