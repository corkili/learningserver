package com.corkili.learningserver.scorm.sn.api.request;

import com.sun.istack.internal.NotNull;

import com.corkili.learningserver.scorm.sn.model.tree.Activity;
import com.corkili.learningserver.scorm.sn.model.tree.ActivityTree;

public class NavigationRequest extends Request {

    private Type requestType;

    public NavigationRequest(@NotNull Type requestType, ActivityTree targetActivityTree, Activity targetActivity) {
        super(targetActivityTree, targetActivity);
        this.requestType = requestType;
    }

    public Type getRequestType() {
        return requestType;
    }

    public NavigationRequest setRequestType(Type requestType) {
        this.requestType = requestType;
        return this;
    }

    @Override
    public NavigationRequest setTargetActivityTree(ActivityTree targetActivityTree) {
        return (NavigationRequest) super.setTargetActivityTree(targetActivityTree);
    }

    @Override
    public NavigationRequest setTargetActivity(Activity targetActivity) {
        return (NavigationRequest) super.setTargetActivity(targetActivity);
    }

    public enum Type {
        Start,
        ResumeAll,
        Continue,
        Previous,
        Forward,
        Backward,
        Choice,
        Jump,
        Exit,
        ExitAll,
        SuspendAll,
        Abandon,
        AbandonAll;
    }

}
