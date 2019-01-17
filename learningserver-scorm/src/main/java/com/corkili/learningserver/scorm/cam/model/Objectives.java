package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

public class Objectives {

    // elements
    private Objective primaryObjective; // 1...1
    private List<Objective> objectiveList; // 0...n

    public Objectives() {
        objectiveList = new ArrayList<>();
    }

    public Objective getPrimaryObjective() {
        return primaryObjective;
    }

    public void setPrimaryObjective(Objective primaryObjective) {
        this.primaryObjective = primaryObjective;
    }

    public List<Objective> getObjectiveList() {
        return objectiveList;
    }

    public void setObjectiveList(List<Objective> objectiveList) {
        this.objectiveList = objectiveList;
    }
}
