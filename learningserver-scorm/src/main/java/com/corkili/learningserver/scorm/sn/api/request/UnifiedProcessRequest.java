package com.corkili.learningserver.scorm.sn.api.request;

import com.corkili.learningserver.scorm.sn.model.definition.SequencingRuleDescription;
import com.corkili.learningserver.scorm.sn.model.definition.SequencingRuleDescription.ConditionType;
import com.corkili.learningserver.scorm.sn.model.tree.Activity;
import com.corkili.learningserver.scorm.sn.model.tree.ActivityTree;

public class UnifiedProcessRequest extends Request {

    private ConditionType conditionType;

    private String[] ruleActions;

    private SequencingRuleDescription sequencingRuleDescription;

    public UnifiedProcessRequest(ActivityTree targetActivityTree, Activity targetActivity) {
        super(targetActivityTree, targetActivity);
    }

    @Override
    public UnifiedProcessRequest setTargetActivityTree(ActivityTree targetActivityTree) {
        return (UnifiedProcessRequest) super.setTargetActivityTree(targetActivityTree);
    }

    @Override
    public UnifiedProcessRequest setTargetActivity(Activity targetActivity) {
        return (UnifiedProcessRequest) super.setTargetActivity(targetActivity);
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public UnifiedProcessRequest setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
        return this;
    }

    public String[] getRuleActions() {
        return ruleActions;
    }

    public UnifiedProcessRequest setRuleActions(String... ruleActions) {
        this.ruleActions = ruleActions;
        return this;
    }

    public SequencingRuleDescription getSequencingRuleDescription() {
        return sequencingRuleDescription;
    }

    public UnifiedProcessRequest setSequencingRuleDescription(SequencingRuleDescription sequencingRuleDescription) {
        this.sequencingRuleDescription = sequencingRuleDescription;
        return this;
    }
}
