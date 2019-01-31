package com.corkili.learningserver.scorm.sn.api.behavior.result;

public class UnifiedProcessResult extends BaseResult {

    private String action;

    private Boolean result;

    private boolean limitConditionViolated;

    public String getAction() {
        return action;
    }

    public UnifiedProcessResult setAction(String action) {
        this.action = action;
        return this;
    }

    public Boolean getResult() {
        return result;
    }

    public UnifiedProcessResult setResult(Boolean result) {
        this.result = result;
        return this;
    }

    public boolean isLimitConditionViolated() {
        return limitConditionViolated;
    }

    public UnifiedProcessResult setLimitConditionViolated(boolean limitConditionViolated) {
        this.limitConditionViolated = limitConditionViolated;
        return this;
    }

    @Override
    public UnifiedProcessResult setException(SequencingException exception) {
        return (UnifiedProcessResult) super.setException(exception);
    }
}
