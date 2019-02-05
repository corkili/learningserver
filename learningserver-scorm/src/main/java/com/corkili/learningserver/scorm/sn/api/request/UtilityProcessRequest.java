package com.corkili.learningserver.scorm.sn.api.request;

import com.corkili.learningserver.scorm.sn.model.definition.SequencingRuleDescription;
import com.corkili.learningserver.scorm.sn.model.definition.SequencingRuleDescription.ConditionType;
import com.corkili.learningserver.scorm.sn.model.tree.Activity;
import com.corkili.learningserver.scorm.sn.model.tree.ActivityTree;

public class UtilityProcessRequest extends Request {

    private ConditionType conditionType;

    private String[] ruleActions;

    private SequencingRuleDescription sequencingRuleDescription;

    public UtilityProcessRequest(ActivityTree targetActivityTree, Activity targetActivity) {
        super(targetActivityTree, targetActivity);
    }

    @Override
    public UtilityProcessRequest setTargetActivityTree(ActivityTree targetActivityTree) {
        return (UtilityProcessRequest) super.setTargetActivityTree(targetActivityTree);
    }

    @Override
    public UtilityProcessRequest setTargetActivity(Activity targetActivity) {
        return (UtilityProcessRequest) super.setTargetActivity(targetActivity);
    }

    public ConditionType getConditionType() {
        return conditionType;
    }

    public UtilityProcessRequest setConditionType(ConditionType conditionType) {
        this.conditionType = conditionType;
        return this;
    }

    public String[] getRuleActions() {
        return ruleActions;
    }

    public UtilityProcessRequest setRuleActions(String... ruleActions) {
        this.ruleActions = ruleActions;
        return this;
    }

    public SequencingRuleDescription getSequencingRuleDescription() {
        return sequencingRuleDescription;
    }

    public UtilityProcessRequest setSequencingRuleDescription(SequencingRuleDescription sequencingRuleDescription) {
        this.sequencingRuleDescription = sequencingRuleDescription;
        return this;
    }
}
