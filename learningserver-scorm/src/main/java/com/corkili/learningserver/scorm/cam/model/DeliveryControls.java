package com.corkili.learningserver.scorm.cam.model;

public class DeliveryControls {

    // attributes
    private boolean tracked; // O true
    private boolean completionSetByContent; // O false
    private boolean objectiveSetByContent; // O false

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
