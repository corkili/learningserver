package com.corkili.learningserver.scorm.sn.api.request;

import com.corkili.learningserver.scorm.sn.model.tree.Activity;
import com.corkili.learningserver.scorm.sn.model.tree.ActivityTree;

public class DeliveryRequest extends Request {

    public DeliveryRequest(ActivityTree targetActivityTree, Activity targetActivity) {
        super(targetActivityTree, targetActivity);
    }

    @Override
    public DeliveryRequest setTargetActivityTree(ActivityTree targetActivityTree) {
        return (DeliveryRequest) super.setTargetActivityTree(targetActivityTree);
    }

    @Override
    public DeliveryRequest setTargetActivity(Activity targetActivity) {
        return (DeliveryRequest) super.setTargetActivity(targetActivity);
    }
}
