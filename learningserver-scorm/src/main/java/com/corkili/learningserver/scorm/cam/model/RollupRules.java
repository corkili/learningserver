package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

import com.corkili.learningserver.scorm.cam.model.datatype.Decimal;

public class RollupRules {

    // attributes
    private boolean rollupObjectiveSatisfied;
    private boolean rollupProgressCompletion;
    private Decimal objectiveMeasureWeight;

    // elements
    private List<RollupRule> rollupRuleList;

}
