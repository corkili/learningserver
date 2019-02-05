package com.corkili.learningserver.scorm.sn.model.tree;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.corkili.learningserver.scorm.common.ID;
import com.corkili.learningserver.scorm.sn.model.definition.ObjectiveDescription;
import com.corkili.learningserver.scorm.sn.model.definition.HideLmsUIControls;
import com.corkili.learningserver.scorm.sn.model.definition.SequencingDefinition;
import com.corkili.learningserver.scorm.sn.model.tracking.ActivityProgressInformation;
import com.corkili.learningserver.scorm.sn.model.tracking.ActivityStateInformation;
import com.corkili.learningserver.scorm.sn.model.tracking.AttemptProgressInformation;

public class Activity {

    private final ID id;

    private final SequencingDefinition sequencingDefinition;

    private final ActivityProgressInformation activityProgressInformation;

    private final AttemptProgressInformation attemptProgressInformation;

    private final ActivityStateInformation activityStateInformation;

    private final HideLmsUIControls hideLmsUIControls;

    private boolean visible;

    private String title;

    private String referenceResource;

    private String parameters;

    private Activity parentActivity;

    private final List<Activity> children;

    public Activity(ID id) {
        this.id = id;
        children = new ArrayList<>();
        sequencingDefinition = new SequencingDefinition();
        activityProgressInformation = new ActivityProgressInformation();
        attemptProgressInformation = new AttemptProgressInformation();
        activityStateInformation = new ActivityStateInformation();
        hideLmsUIControls = new HideLmsUIControls();
        visible = true;
        title = "";
        referenceResource = "";
        parameters = "";
    }

    public SequencingDefinition getSequencingDefinition() {
        return sequencingDefinition;
    }

    public ActivityProgressInformation getActivityProgressInformation() {
        return activityProgressInformation;
    }

    public AttemptProgressInformation getAttemptProgressInformation() {
        return attemptProgressInformation;
    }

    public ActivityStateInformation getActivityStateInformation() {
        return activityStateInformation;
    }

    public HideLmsUIControls getHideLmsUIControls() {
        return hideLmsUIControls;
    }

    public boolean isVisible() {
        return visible;
    }

    public Activity setVisible(boolean visible) {
        this.visible = visible;
        return this;
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

    public ID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Activity setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getReferenceResource() {
        return referenceResource;
    }

    public Activity setReferenceResource(String referenceResource) {
        this.referenceResource = referenceResource;
        return this;
    }

    public String getParameters() {
        return parameters;
    }

    public Activity setParameters(String parameters) {
        this.parameters = parameters;
        return this;
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
