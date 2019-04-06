package com.corkili.learningserver.scorm.sn.model.tracking;

import java.time.Duration;

import com.corkili.learningserver.scorm.sn.model.datatype.NonNegativeInteger;
import com.corkili.learningserver.scorm.sn.model.tree.Activity;

/**
 * Each tracked activity has one set of tracking status information that span all attempts on that activity.
 */
public class ActivityProgressInformation {

    private final Activity context;
    private boolean activityProgressStatus;
    private Duration activityAbsoluteDuration;
    private Duration activityExperiencedDuration;
    private final NonNegativeInteger activityAttemptCount;

    public ActivityProgressInformation(Activity context) {
        this.context = context;
        activityProgressStatus = false;
        activityAbsoluteDuration = Duration.ZERO;
        activityExperiencedDuration = Duration.ZERO;
        activityAttemptCount = new NonNegativeInteger(0);
    }

    public Activity getContext() {
        return context;
    }

    public boolean isActivityProgressStatus() {
        return activityProgressStatus;
    }

    public ActivityProgressInformation setActivityProgressStatus(boolean activityProgressStatus) {
        this.activityProgressStatus = activityProgressStatus;
        return this;
    }

    public Duration getActivityAbsoluteDuration() {
        return activityAbsoluteDuration;
    }

    public ActivityProgressInformation setActivityAbsoluteDuration(Duration activityAbsoluteDuration) {
        this.activityAbsoluteDuration = activityAbsoluteDuration;
        return this;
    }

    public Duration getActivityExperiencedDuration() {
        return activityExperiencedDuration;
    }

    public ActivityProgressInformation setActivityExperiencedDuration(Duration activityExperiencedDuration) {
        this.activityExperiencedDuration = activityExperiencedDuration;
        return this;
    }

    public NonNegativeInteger getActivityAttemptCount() {
        return activityAttemptCount;
    }
}
