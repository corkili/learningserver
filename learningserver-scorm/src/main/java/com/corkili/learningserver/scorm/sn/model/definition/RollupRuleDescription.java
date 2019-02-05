package com.corkili.learningserver.scorm.sn.model.definition;

import java.util.ArrayList;
import java.util.List;

import com.corkili.learningserver.scorm.sn.model.datatype.DecimalWithRange;
import com.corkili.learningserver.scorm.sn.model.datatype.NonNegativeInteger;
import com.corkili.learningserver.scorm.sn.model.datatype.Vocabulary;

/**
 * @see  com.corkili.learningserver.scorm.cam.model.RollupRule
 */
public class RollupRuleDescription implements DefinitionElementSet {

    private final Vocabulary conditionCombination;
    private final List<RollupCondition> rollupConditions;
    private final Vocabulary childActivitySet;
    private final NonNegativeInteger rollupMinimumCount;
    private final DecimalWithRange rollupMinimumPercent;
    private final Vocabulary rollupAction;

    public RollupRuleDescription() {
        conditionCombination = new Vocabulary("Any", "All", "Any");
        rollupConditions = new ArrayList<>();
        childActivitySet = new Vocabulary("All", "All", "Any", "None", "At Least Count", "At Least Percent");
        rollupMinimumCount = new NonNegativeInteger(0);
        rollupMinimumPercent = new DecimalWithRange(0, 0, 1, 4);
        rollupAction = new Vocabulary("Satisfied", "Satisfied", "Not Satisfied", "Completed", "Incomplete");
    }

    public Vocabulary getConditionCombination() {
        return conditionCombination;
    }

    public List<RollupCondition> getRollupConditions() {
        return rollupConditions;
    }

    public Vocabulary getChildActivitySet() {
        return childActivitySet;
    }

    public NonNegativeInteger getRollupMinimumCount() {
        return rollupMinimumCount;
    }

    public DecimalWithRange getRollupMinimumPercent() {
        return rollupMinimumPercent;
    }

    public Vocabulary getRollupAction() {
        return rollupAction;
    }
}
