package com.corkili.learningserver.scorm;

import com.corkili.learningserver.scorm.cam.model.DeliveryContent;

public class SCORMResult {

    private final boolean success;

    private final DeliveryContent deliveryContent;

    private final String errorMsg;

    public SCORMResult() {
        this(true, null, "");
    }

    public SCORMResult(DeliveryContent deliveryContent) {
        this(true, deliveryContent, "");
    }

    public SCORMResult(String errorMsg) {
        this(false, null, errorMsg);
    }

    public SCORMResult(boolean success, DeliveryContent deliveryContent, String errorMsg) {
        this.success = success;
        this.deliveryContent = deliveryContent;
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public DeliveryContent getDeliveryContent() {
        return deliveryContent;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
