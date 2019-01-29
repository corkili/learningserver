package com.corkili.learningserver.scorm.sn.tree;

import java.util.ArrayList;
import java.util.List;

import com.corkili.learningserver.scorm.sn.model.definition.SequencingDefinition;
import com.corkili.learningserver.scorm.sn.model.tracking.ActivityProgressInformation;
import com.corkili.learningserver.scorm.sn.model.tracking.ActivityStateInformation;
import com.corkili.learningserver.scorm.sn.model.tracking.AttemptProgressInformation;

public class Activity {

    private SequencingDefinition sequencingDefinition;

    private ActivityProgressInformation activityProgressInformation;

    private AttemptProgressInformation attemptProgressInformation;

    private ActivityStateInformation activityStateInformation;

    private final List<Activity> children;

    public Activity() {
        children = new ArrayList<>();
    }

    public SequencingDefinition getSequencingDefinition() {
        return sequencingDefinition;
    }

    public void setSequencingDefinition(SequencingDefinition sequencingDefinition) {
        this.sequencingDefinition = sequencingDefinition;
    }

    public ActivityProgressInformation getActivityProgressInformation() {
        return activityProgressInformation;
    }

    public void setActivityProgressInformation(ActivityProgressInformation activityProgressInformation) {
        this.activityProgressInformation = activityProgressInformation;
    }

    public AttemptProgressInformation getAttemptProgressInformation() {
        return attemptProgressInformation;
    }

    public void setAttemptProgressInformation(AttemptProgressInformation attemptProgressInformation) {
        this.attemptProgressInformation = attemptProgressInformation;
    }

    public ActivityStateInformation getActivityStateInformation() {
        return activityStateInformation;
    }

    public void setActivityStateInformation(ActivityStateInformation activityStateInformation) {
        this.activityStateInformation = activityStateInformation;
    }

    public List<Activity> getChildren() {
        return children;
    }
}
