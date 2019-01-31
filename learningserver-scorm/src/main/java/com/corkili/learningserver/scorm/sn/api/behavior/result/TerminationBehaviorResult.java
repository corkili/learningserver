package com.corkili.learningserver.scorm.sn.api.behavior.result;

import com.corkili.learningserver.scorm.sn.api.request.SequencingRequest;
import com.corkili.learningserver.scorm.sn.api.request.TerminationRequest;

public class TerminationBehaviorResult extends BaseResult {

    private boolean validTerminationRequest;

    private SequencingRequest sequencingRequest;

    private TerminationRequest terminationRequest;

    public boolean isValidTerminationRequest() {
        return validTerminationRequest;
    }

    public TerminationBehaviorResult setValidTerminationRequest(boolean validTerminationRequest) {
        this.validTerminationRequest = validTerminationRequest;
        return this;
    }

    public SequencingRequest getSequencingRequest() {
        return sequencingRequest;
    }

    public TerminationBehaviorResult setSequencingRequest(SequencingRequest sequencingRequest) {
        this.sequencingRequest = sequencingRequest;
        return this;
    }

    public TerminationRequest getTerminationRequest() {
        return terminationRequest;
    }

    public TerminationBehaviorResult setTerminationRequest(TerminationRequest terminationRequest) {
        this.terminationRequest = terminationRequest;
        return this;
    }

    @Override
    public TerminationBehaviorResult setException(SequencingException exception) {
        return (TerminationBehaviorResult) super.setException(exception);
    }
}
