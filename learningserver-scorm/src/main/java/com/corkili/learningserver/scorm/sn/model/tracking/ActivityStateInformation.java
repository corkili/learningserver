package com.corkili.learningserver.scorm.sn.model.tracking;

import java.util.ArrayList;
import java.util.List;

import com.corkili.learningserver.scorm.sn.tree.Activity;

/**
 * For each activity in the activity tree on a per learner basis.
 * This information exists for each activity regardless if the activity is tracked out.
 */
public class ActivityStateInformation {

    private boolean activityIsActive;
    private boolean activityIsSuspended;
    private final List<Activity> availableChildren;

    public ActivityStateInformation() {
        activityIsActive = false;
        activityIsSuspended = false;
        availableChildren = new ArrayList<>();
    }

    public boolean isActivityIsActive() {
        return activityIsActive;
    }

    public void setActivityIsActive(boolean activityIsActive) {
        this.activityIsActive = activityIsActive;
    }

    public boolean isActivityIsSuspended() {
        return activityIsSuspended;
    }

    public void setActivityIsSuspended(boolean activityIsSuspended) {
        this.activityIsSuspended = activityIsSuspended;
    }

    public List<Activity> getAvailableChildren() {
        return availableChildren;
    }
}
