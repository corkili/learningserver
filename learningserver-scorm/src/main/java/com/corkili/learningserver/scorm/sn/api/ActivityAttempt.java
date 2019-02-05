package com.corkili.learningserver.scorm.sn.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.corkili.learningserver.scorm.common.ID;
import com.corkili.learningserver.scorm.sn.model.tree.Activity;

public final class ActivityAttempt {

    private final ID id;

    private final Activity targetActivity;

    private final ActivityAttempt parentAttempt;

    private final List<ActivityAttempt> childrenAttempt;

    private final boolean isRoot;

    private boolean state;

    public ActivityAttempt(Activity targetActivity, ActivityAttempt parentAttempt) {
        this.targetActivity = targetActivity;
        this.parentAttempt = parentAttempt;
        this.childrenAttempt = new ArrayList<>();
        this.id = targetActivity.getId();
        this.isRoot = parentAttempt == null;
        if (parentAttempt != null) {
            parentAttempt.childrenAttempt.add(this);
        }
        active();
    }

    public final ID getId() {
        return id;
    }

    public Activity getTargetActivity() {
        return targetActivity;
    }

    public ActivityAttempt getParentAttempt() {
        return parentAttempt;
    }

    public List<ActivityAttempt> getChildrenAttempt() {
        return childrenAttempt;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void active() {
        this.state = true;
    }

    public void suspend() {
        this.state = false;
    }

    public boolean isActive() {
        return state;
    }

    public boolean isSuspend() {
        return !state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ActivityAttempt that = (ActivityAttempt) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }
}

