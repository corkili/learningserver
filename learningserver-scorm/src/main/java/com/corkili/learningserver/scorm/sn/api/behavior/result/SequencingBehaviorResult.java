package com.corkili.learningserver.scorm.sn.api.behavior.result;

import com.corkili.learningserver.scorm.sn.api.behavior.common.TraversalDirection;
import com.corkili.learningserver.scorm.sn.api.request.DeliveryRequest;
import com.corkili.learningserver.scorm.sn.model.tree.Activity;

public class SequencingBehaviorResult extends BaseResult {

    private boolean validSequencingRequest;
    private DeliveryRequest deliveryRequest;
    private Boolean endSequencingSession;
    private Activity nextActivity;
    private TraversalDirection traversalDirection;
    private boolean deliverable;
    private Activity identifiedActivity;
    private boolean reachable;

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

    public Boolean getEndSequencingSession() {
        return endSequencingSession;
    }

    public SequencingBehaviorResult setEndSequencingSession(Boolean endSequencingSession) {
        this.endSequencingSession = endSequencingSession;
        return this;
    }

    public Activity getNextActivity() {
        return nextActivity;
    }

    public SequencingBehaviorResult setNextActivity(Activity nextActivity) {
        this.nextActivity = nextActivity;
        return this;
    }

    public TraversalDirection getTraversalDirection() {
        return traversalDirection;
    }

    public SequencingBehaviorResult setTraversalDirection(TraversalDirection traversalDirection) {
        this.traversalDirection = traversalDirection;
        return this;
    }

    public boolean isDeliverable() {
        return deliverable;
    }

    public SequencingBehaviorResult setDeliverable(boolean deliverable) {
        this.deliverable = deliverable;
        return this;
    }

    public Activity getIdentifiedActivity() {
        return identifiedActivity;
    }

    public SequencingBehaviorResult setIdentifiedActivity(Activity identifiedActivity) {
        this.identifiedActivity = identifiedActivity;
        return this;
    }

    public boolean isReachable() {
        return reachable;
    }

    public SequencingBehaviorResult setReachable(boolean reachable) {
        this.reachable = reachable;
        return this;
    }

    @Override
    public SequencingBehaviorResult setException(SequencingException exception) {
        return (SequencingBehaviorResult) super.setException(exception);
    }
}
