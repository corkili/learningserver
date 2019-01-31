package com.corkili.learningserver.scorm.sn.model.tracking;

import java.util.Objects;

import com.corkili.learningserver.scorm.sn.model.tree.Activity;

/**
 * for an activity tree
 */
public class GlobalStateInformation {

    private Activity currentActivity;
    private Activity suspendedActivity;

    public GlobalStateInformation() {
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
        if (Objects.equals(currentActivity, suspendedActivity)) {
            suspendedActivity = null;
        }
    }

    public Activity getSuspendedActivity() {
        return suspendedActivity;
    }

    public void setSuspendedActivity(Activity suspendedActivity) {
        this.suspendedActivity = suspendedActivity;
        if (Objects.equals(suspendedActivity, currentActivity)) {
            currentActivity = null;
        }
    }
}
