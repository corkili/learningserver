package com.corkili.learningserver.scorm.sn.model.tracking;

import java.time.Duration;

import com.corkili.learningserver.scorm.sn.model.datatype.NonNegativeInteger;

/**
 * Each tracked activity has one set of tracking status information that span all attempts on that activity.
 */
public class ActivityProgressInformation {

    private boolean activityProgressStatus;
    private Duration activityAbsoluteDuration;
    private Duration activityExperiencedDuration;
    private final NonNegativeInteger activityAttemptCount;

    public ActivityProgressInformation() {
        activityProgressStatus = false;
        activityAbsoluteDuration = Duration.ZERO;
        activityExperiencedDuration = Duration.ZERO;
        activityAttemptCount = new NonNegativeInteger(0);
    }
}
