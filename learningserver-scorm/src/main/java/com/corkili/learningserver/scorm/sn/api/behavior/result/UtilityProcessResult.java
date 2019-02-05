package com.corkili.learningserver.scorm.sn.api.behavior.result;

public class UtilityProcessResult extends BaseResult {

    private String action;

    private Boolean result;

    private boolean limitConditionViolated;

    public String getAction() {
        return action;
    }

    public UtilityProcessResult setAction(String action) {
        this.action = action;
        return this;
    }

    public Boolean getResult() {
        return result;
    }

    public UtilityProcessResult setResult(Boolean result) {
        this.result = result;
        return this;
    }

    public boolean isLimitConditionViolated() {
        return limitConditionViolated;
    }

    public UtilityProcessResult setLimitConditionViolated(boolean limitConditionViolated) {
        this.limitConditionViolated = limitConditionViolated;
        return this;
    }

    @Override
    public UtilityProcessResult setException(SequencingException exception) {
        return (UtilityProcessResult) super.setException(exception);
    }
}
