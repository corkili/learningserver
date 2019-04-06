package com.corkili.learningserver.scorm;

import com.corkili.learningserver.scorm.cam.model.DeliveryContent;
import com.corkili.learningserver.scorm.sn.model.tree.Activity;

public class SCORMResult {

    private final boolean success;

    private final DeliveryContent deliveryContent;

    private final Activity deliveryActivity;

    private final String errorMsg;

    public SCORMResult() {
        this(true, null, null, "");
    }

    public SCORMResult(DeliveryContent deliveryContent, Activity deliveryActivity) {
        this(true, deliveryContent, deliveryActivity, "");
    }

    public SCORMResult(String errorMsg) {
        this(false, null, null, errorMsg);
    }

    public SCORMResult(boolean success, DeliveryContent deliveryContent, Activity deliveryActivity, String errorMsg) {
        this.success = success;
        this.deliveryContent = deliveryContent;
        this.deliveryActivity = deliveryActivity;
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public DeliveryContent getDeliveryContent() {
        return deliveryContent;
    }

    public Activity getDeliveryActivity() {
        return deliveryActivity;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
