package com.corkili.learningserver.scorm.sn.api.behavior.result;

import com.corkili.learningserver.scorm.sn.api.behavior.rto.EndSequencingSession;
import com.corkili.learningserver.scorm.sn.api.request.DeliveryRequest;

public class SequencingBehaviorResult extends BaseResult {

    private boolean validSequencingRequest;
    private DeliveryRequest deliveryRequest;
    private EndSequencingSession endSequencingSession;

    public boolean isValidSequencingRequest() {
        return validSequencingRequest;
    }

    public SequencingBehaviorResult setValidSequencingRequest(boolean validSequencingRequest) {
        this.validSequencingRequest = validSequencingRequest;
        return this;
    }

    public DeliveryRequest getDeliveryRequest() {
        return deliveryRequest;
    }

    public SequencingBehaviorResult setDeliveryRequest(DeliveryRequest deliveryRequest) {
        this.deliveryRequest = deliveryRequest;
        return this;
    }

    public EndSequencingSession getEndSequencingSession() {
        return endSequencingSession;
    }

    public SequencingBehaviorResult setEndSequencingSession(EndSequencingSession endSequencingSession) {
        this.endSequencingSession = endSequencingSession;
        return this;
    }

    @Override
    public SequencingBehaviorResult setException(SequencingException exception) {
        return (SequencingBehaviorResult) super.setException(exception);
    }
}
