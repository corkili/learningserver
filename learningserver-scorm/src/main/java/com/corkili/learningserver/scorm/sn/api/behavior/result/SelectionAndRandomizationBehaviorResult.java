package com.corkili.learningserver.scorm.sn.api.behavior.result;

public class SelectionAndRandomizationBehaviorResult extends BaseResult {

    @Override
    public SelectionAndRandomizationBehaviorResult setException(SequencingException exception) {
        return (SelectionAndRandomizationBehaviorResult) super.setException(exception);
    }
}
