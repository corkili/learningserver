package com.corkili.learningserver.scorm.sn.api.request;

import com.corkili.learningserver.scorm.sn.model.tree.Activity;
import com.corkili.learningserver.scorm.sn.model.tree.ActivityTree;

public abstract class Request {

    private ActivityTree targetActivityTree;

    private Activity targetActivity;

    public Request(ActivityTree targetActivityTree, Activity targetActivity) {
        this.targetActivityTree = targetActivityTree;
        this.targetActivity = targetActivity;
    }

    public ActivityTree getTargetActivityTree() {
        return targetActivityTree;
    }

    public Request setTargetActivityTree(ActivityTree targetActivityTree) {
        this.targetActivityTree = targetActivityTree;
        return this;
    }

    public Activity getTargetActivity() {
        return targetActivity;
    }

    public Request setTargetActivity(Activity targetActivity) {
        this.targetActivity = targetActivity;
        return this;
    }
}
