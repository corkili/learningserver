package com.corkili.learningserver.scorm.sn.api.request;

import com.sun.istack.internal.NotNull;

import com.corkili.learningserver.scorm.sn.model.tree.Activity;
import com.corkili.learningserver.scorm.sn.model.tree.ActivityTree;

public class SequencingRequest extends Request {

    private Type requestType;

    public SequencingRequest(@NotNull Type requestType, ActivityTree targetActivityTree, Activity targetActivity) {
        super(targetActivityTree, targetActivity);
        this.requestType = requestType;
    }

    public Type getRequestType() {
        return requestType;
    }

    public SequencingRequest setRequestType(Type requestType) {
        this.requestType = requestType;
        return this;
    }

    @Override
    public SequencingRequest setTargetActivityTree(ActivityTree targetActivityTree) {
        return (SequencingRequest) super.setTargetActivityTree(targetActivityTree);
    }

    @Override
    public SequencingRequest setTargetActivity(Activity targetActivity) {
        return (SequencingRequest) super.setTargetActivity(targetActivity);
    }

    public enum Type {
        Start,
        ResumeAll,
        Continue,
        Previous,
        Choice,
        Retry,
        Exit,
        Jump
    }

}
