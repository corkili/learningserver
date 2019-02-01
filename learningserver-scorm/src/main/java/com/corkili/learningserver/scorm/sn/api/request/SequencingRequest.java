package com.corkili.learningserver.scorm.sn.api.request;

import com.sun.istack.internal.NotNull;

import com.corkili.learningserver.scorm.sn.api.behavior.common.TraversalDirection;
import com.corkili.learningserver.scorm.sn.model.tree.Activity;
import com.corkili.learningserver.scorm.sn.model.tree.ActivityTree;

public class SequencingRequest extends Request {

    private Type requestType;

    private TraversalDirection traversalDirection;

    private TraversalDirection previousTraversalDirection;

    private boolean considerChildren;

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

    public TraversalDirection getTraversalDirection() {
        return traversalDirection;
    }

    public SequencingRequest setTraversalDirection(TraversalDirection traversalDirection) {
        this.traversalDirection = traversalDirection;
        return this;
    }

    public TraversalDirection getPreviousTraversalDirection() {
        return previousTraversalDirection;
    }

    public SequencingRequest setPreviousTraversalDirection(TraversalDirection previousTraversalDirection) {
        this.previousTraversalDirection = previousTraversalDirection;
        return this;
    }

    public boolean isConsiderChildren() {
        return considerChildren;
    }

    public SequencingRequest setConsiderChildren(boolean considerChildren) {
        this.considerChildren = considerChildren;
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
