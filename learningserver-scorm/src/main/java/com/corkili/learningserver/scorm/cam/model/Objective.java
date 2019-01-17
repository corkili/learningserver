package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

import com.corkili.learningserver.scorm.cam.model.datatype.AnyURI;
import com.corkili.learningserver.scorm.cam.model.datatype.Decimal;

public class Objective {

    // attributes
    private boolean satisfiedByMeasure; // O false
    private AnyURI objectiveID; // O(for primaryObjective) M(for Objective)

    // elements
    private Decimal minNormalizedMeasure; // 0...1
    private List<MapInfo> mapInfoList; // 0...n

}
