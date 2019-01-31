package com.corkili.learningserver.scorm.sn.api.behavior.result;

public class DeliveryBehaviorResult extends BaseResult {

    private boolean validDeliveryRequest;

    public boolean isValidDeliveryRequest() {
        return validDeliveryRequest;
    }

    public DeliveryBehaviorResult setValidDeliveryRequest(boolean validDeliveryRequest) {
        this.validDeliveryRequest = validDeliveryRequest;
        return this;
    }

    @Override
    public DeliveryBehaviorResult setException(SequencingException exception) {
        return (DeliveryBehaviorResult) super.setException(exception);
    }
}
