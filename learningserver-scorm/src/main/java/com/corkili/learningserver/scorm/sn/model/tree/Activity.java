package com.corkili.learningserver.scorm.sn.model.tree;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.corkili.learningserver.scorm.sn.model.definition.ObjectiveDescription;
import com.corkili.learningserver.scorm.sn.model.definition.SequencingDefinition;
import com.corkili.learningserver.scorm.sn.model.tracking.ActivityProgressInformation;
import com.corkili.learningserver.scorm.sn.model.tracking.ActivityStateInformation;
import com.corkili.learningserver.scorm.sn.model.tracking.AttemptProgressInformation;

public class Activity {

    private String id;

    private SequencingDefinition sequencingDefinition;

    private ActivityProgressInformation activityProgressInformation;

    private AttemptProgressInformation attemptProgressInformation;

    private ActivityStateInformation activityStateInformation;

    private Activity parentActivity;

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

    public Activity getParentActivity() {
        return parentActivity;
    }

    public void setParentActivity(Activity parentActivity) {
        this.parentActivity = parentActivity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void addChildren(Activity activity) {
        if (activity != null) {
            this.children.add(activity);
        }
    }

    public boolean isSiblingActivity(Activity targetActivity) {
        if (parentActivity == null || targetActivity == null || targetActivity.getParentActivity() == null) {
            return false;
        }
        return parentActivity.equals(targetActivity.getParentActivity());
    }

    public ObjectiveDescription findAssociatedObjectiveByID(String objectiveID) {
        for (ObjectiveDescription objectiveDescription : sequencingDefinition.getObjectiveDescriptions()) {
            if (StringUtils.equals(objectiveID, objectiveDescription.getObjectiveID())) {
                return objectiveDescription;
            }
        }
        return null;
    }

    public ObjectiveDescription findAssociatedObjectiveForContributingToRollup() {
        for (ObjectiveDescription objectiveDescription : sequencingDefinition.getObjectiveDescriptions()) {
            if (objectiveDescription.isObjectiveContributesToRollup()) {
                return objectiveDescription;
            }
        }
        return null;
    }

    public boolean isDescendent(Activity activity) {
        if (activity == null || isLeaf()) {
            return false;
        }
        if (activity.equals(this)) {
            return false;
        }
        Activity parent = activity.getParentActivity();
        while (parent != null) {
            if (parent.equals(this)) {
                return true;
            }
            parent = parent.getParentActivity();
        }
        return false;
    }

    public boolean isLeaf() {
        return this.children.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Activity activity = (Activity) o;

        return new EqualsBuilder()
                .append(id, activity.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }
}
