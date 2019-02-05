package com.corkili.learningserver.scorm.sn.api;

import com.corkili.learningserver.scorm.common.CommonUtils;
import com.corkili.learningserver.scorm.sn.api.behavior.result.SequencingException;
import com.corkili.learningserver.scorm.sn.model.tree.Activity;

public final class ProcessResult {

    private final boolean success;
    private final Activity deliveryActivity;
    private final String errorMsg;

    public ProcessResult() {
        this(true, null, "");
    }

    public ProcessResult(String errorMsg) {
        this(false, null, errorMsg);
    }

    public ProcessResult(Activity deliveryActivity) {
        this(true, deliveryActivity, "");
    }

    public ProcessResult(SequencingException sequencingException) {
        this(false, null, CommonUtils.format("Sequencing Exception: {} - {}",
                sequencingException.getCode(), sequencingException.getDescription()));
    }

    private ProcessResult(boolean success, Activity deliveryActivity, String errorMsg) {
        this.success = success;
        this.deliveryActivity = deliveryActivity;
        this.errorMsg = errorMsg;
    }

    public boolean isSuccess() {
        return success;
    }

    public Activity getDeliveryActivity() {
        return deliveryActivity;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
