package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

import com.corkili.learningserver.scorm.cam.model.datatype.AnyURI;

public class AdlseqObjective {

    // attributes
    private AnyURI objectiveID; // M

    // elements
    private List<AdlseqMapInfo> mapInfoList; // 1...n

}
