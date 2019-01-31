package com.corkili.learningserver.scorm.sn.api;

import com.corkili.learningserver.scorm.sn.model.tree.Activity;

public class ActivityAttempt {

    private final Activity targetActivity;

    private final AttemptContext attemptContext;

    private ActivityAttempt(Activity targetActivity) {
        this.targetActivity = targetActivity;
        attemptContext = new AttemptContext(null);
    }

    private ActivityAttempt(Activity targetActivity, AttemptContext parentAttemptActivityContext) {
        this.targetActivity = targetActivity;
        this.attemptContext = new AttemptContext(parentAttemptActivityContext);
    }

    public static ActivityAttempt createRootAttempt(Activity targetActivity) {
        return new ActivityAttempt(targetActivity);
    }

    public static ActivityAttempt createChildAttempt(Activity targetActivity, ActivityAttempt parentAttemptActivity) {
        return new ActivityAttempt(targetActivity, parentAttemptActivity.attemptContext);
    }
}
