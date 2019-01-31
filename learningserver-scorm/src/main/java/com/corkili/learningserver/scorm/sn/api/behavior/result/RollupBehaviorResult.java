package com.corkili.learningserver.scorm.sn.api.behavior.result;

public class RollupBehaviorResult extends BaseResult {

    private boolean childIsIncludedInRollup;

    private Boolean evaluation;

    public boolean isChildIsIncludedInRollup() {
        return childIsIncludedInRollup;
    }

    public RollupBehaviorResult setChildIsIncludedInRollup(boolean childIsIncludedInRollup) {
        this.childIsIncludedInRollup = childIsIncludedInRollup;
        return this;
    }

    public Boolean getEvaluation() {
        return evaluation;
    }

    public RollupBehaviorResult setEvaluation(Boolean evaluation) {
        this.evaluation = evaluation;
        return this;
    }

    @Override
    public RollupBehaviorResult setException(SequencingException exception) {
        return (RollupBehaviorResult) super.setException(exception);
    }
}
