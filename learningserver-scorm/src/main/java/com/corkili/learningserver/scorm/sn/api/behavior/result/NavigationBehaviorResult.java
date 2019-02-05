package com.corkili.learningserver.scorm.sn.api.behavior.result;

import com.corkili.learningserver.scorm.sn.api.request.SequencingRequest;
import com.corkili.learningserver.scorm.sn.api.request.TerminationRequest;

public class NavigationBehaviorResult extends BaseResult {

    private boolean validNavigationRequest;
    private TerminationRequest terminationRequest;
    private SequencingRequest sequencingRequest;

    public boolean isValidNavigationRequest() {
        return validNavigationRequest;
    }

    public NavigationBehaviorResult setValidNavigationRequest(boolean validNavigationRequest) {
        this.validNavigationRequest = validNavigationRequest;
        return this;
    }

    public TerminationRequest getTerminationRequest() {
        return terminationRequest;
    }

    public NavigationBehaviorResult setTerminationRequest(TerminationRequest terminationRequest) {
        this.terminationRequest = terminationRequest;
        return this;
    }

    public SequencingRequest getSequencingRequest() {
        return sequencingRequest;
    }

    public NavigationBehaviorResult setSequencingRequest(SequencingRequest sequencingRequest) {
        this.sequencingRequest = sequencingRequest;
        return this;
    }

    @Override
    public NavigationBehaviorResult setException(SequencingException exception) {
        return (NavigationBehaviorResult) super.setException(exception);
    }
}
