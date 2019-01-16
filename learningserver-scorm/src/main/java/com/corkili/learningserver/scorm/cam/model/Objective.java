package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

import com.corkili.learningserver.scorm.cam.model.datatype.AnyURI;
import com.corkili.learningserver.scorm.cam.model.datatype.Decimal;

public class Objective {

    // attributes
    private boolean satisfiedByMeasure;
    private AnyURI objectiveID;

    // elements
    private Decimal minNormalizedMeasure;
    private List<MapInfo> mapInfoList;

}
