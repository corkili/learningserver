package com.corkili.learningserver.scorm.sn.api.request;

import com.sun.istack.internal.NotNull;

import com.corkili.learningserver.scorm.sn.model.tree.Activity;
import com.corkili.learningserver.scorm.sn.model.tree.ActivityTree;

public class TerminationRequest extends Request {

    private Type requestType;

    public TerminationRequest(@NotNull Type requestType, ActivityTree targetActivityTree, Activity targetActivity) {
        super(targetActivityTree, targetActivity);
        this.requestType = requestType;
    }

    public Type getRequestType() {
        return requestType;
    }

    public TerminationRequest setRequestType(Type requestType) {
        this.requestType = requestType;
        return this;
    }

    @Override
    public TerminationRequest setTargetActivityTree(ActivityTree targetActivityTree) {
        return (TerminationRequest) super.setTargetActivityTree(targetActivityTree);
    }

    @Override
    public TerminationRequest setTargetActivity(Activity targetActivity) {
        return (TerminationRequest) super.setTargetActivity(targetActivity);
    }

    public enum Type {
        Exit,
        ExitParent,
        ExitAll,
        SuspendAll,
        Abandon,
        AbandonAll
    }

}
