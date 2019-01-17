package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

import com.corkili.learningserver.scorm.cam.model.datatype.Decimal;

public class RollupRules {

    // attributes
    private boolean rollupObjectiveSatisfied; // O true
    private boolean rollupProgressCompletion; // O true
    private Decimal objectiveMeasureWeight; // O 1.0000

    // elements
    private List<RollupRule> rollupRuleList; // 0...n

}
