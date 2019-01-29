package com.corkili.learningserver.scorm.sn.model.definition;

/**
 * @see com.corkili.learningserver.scorm.cam.model.DeliveryControls
 */
public class DeliveryControls implements DefinitionElementSet {

    private boolean tracked;
    private boolean completionSetByContent;
    private boolean objectiveSetByContent;

    public DeliveryControls() {
        tracked = true;
        completionSetByContent = false;
        objectiveSetByContent = false;
    }

    public boolean isTracked() {
        return tracked;
    }

    public void setTracked(boolean tracked) {
        this.tracked = tracked;
    }

    public boolean isCompletionSetByContent() {
        return completionSetByContent;
    }

    public void setCompletionSetByContent(boolean completionSetByContent) {
        this.completionSetByContent = completionSetByContent;
    }

    public boolean isObjectiveSetByContent() {
        return objectiveSetByContent;
    }

    public void setObjectiveSetByContent(boolean objectiveSetByContent) {
        this.objectiveSetByContent = objectiveSetByContent;
    }
}
