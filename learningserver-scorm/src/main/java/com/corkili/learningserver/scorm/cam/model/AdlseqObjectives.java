package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

public class AdlseqObjectives {

    // elements
    private List<AdlseqObjective> objectiveList; // 1...n

    public AdlseqObjectives() {
        objectiveList = new ArrayList<>();
    }

    public List<AdlseqObjective> getObjectiveList() {
        return objectiveList;
    }

    public void setObjectiveList(List<AdlseqObjective> objectiveList) {
        this.objectiveList = objectiveList;
    }
}
