package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

import com.corkili.learningserver.scorm.cam.model.datatype.Token;

public class RollupConditions {

    // attributes
    private Token conditionCombination; // O all

    // elements
    private List<RollupCondition> rollupConditionList; // 1...n

}
