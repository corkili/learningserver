package com.corkili.learningserver.scorm.sn.api.request;

import com.corkili.learningserver.scorm.sn.model.definition.RollupRuleDescription;
import com.corkili.learningserver.scorm.sn.model.tree.Activity;
import com.corkili.learningserver.scorm.sn.model.tree.ActivityTree;

public class RollupRequest extends Request {

    private String rollupAction;

    private RollupRuleDescription rollupRule;

    private boolean useDefaultRollupRule;

    private RollupRuleDescription defaultRollupRule;

    public RollupRequest(ActivityTree targetActivityTree, Activity targetActivity) {
        super(targetActivityTree, targetActivity);
    }

    @Override
    public RollupRequest setTargetActivityTree(ActivityTree targetActivityTree) {
        return (RollupRequest) super.setTargetActivityTree(targetActivityTree);
    }

    @Override
    public RollupRequest setTargetActivity(Activity targetActivity) {
        return (RollupRequest) super.setTargetActivity(targetActivity);
    }

    public String getRollupAction() {
        return rollupAction;
    }

    public RollupRequest setRollupAction(String rollupAction) {
        this.rollupAction = rollupAction;
        return this;
    }

    public RollupRuleDescription getRollupRule() {
        return rollupRule;
    }

    public RollupRequest setRollupRule(RollupRuleDescription rollupRule) {
        this.rollupRule = rollupRule;
        return this;
    }

    public boolean isUseDefaultRollupRule() {
        return useDefaultRollupRule;
    }

    public RollupRequest setUseDefaultRollupRule(boolean useDefaultRollupRule) {
        this.useDefaultRollupRule = useDefaultRollupRule;
        return this;
    }

    public RollupRuleDescription getDefaultRollupRule() {
        return defaultRollupRule;
    }

    public RollupRequest setDefaultRollupRule(RollupRuleDescription defaultRollupRule) {
        this.defaultRollupRule = defaultRollupRule;
        return this;
    }
}
