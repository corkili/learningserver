package com.corkili.learningserver.scorm.sn.api.request;

import com.corkili.learningserver.scorm.sn.model.tree.Activity;
import com.corkili.learningserver.scorm.sn.model.tree.ActivityTree;

public class SelectionAndRandomizationRequest extends Request {

    public SelectionAndRandomizationRequest(ActivityTree targetActivityTree, Activity targetActivity) {
        super(targetActivityTree, targetActivity);
    }

    @Override
    public SelectionAndRandomizationRequest setTargetActivityTree(ActivityTree targetActivityTree) {
        return (SelectionAndRandomizationRequest) super.setTargetActivityTree(targetActivityTree);
    }

    @Override
    public SelectionAndRandomizationRequest setTargetActivity(Activity targetActivity) {
        return (SelectionAndRandomizationRequest) super.setTargetActivity(targetActivity);
    }
}
