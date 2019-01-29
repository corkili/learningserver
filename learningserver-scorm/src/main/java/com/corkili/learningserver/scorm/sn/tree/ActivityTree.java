package com.corkili.learningserver.scorm.sn.tree;

import com.corkili.learningserver.scorm.sn.model.tracking.GlobalStateInformation;

public class ActivityTree {

    private boolean objectivesGlobalToSystem; // SN-3-39 3.10.2
    private Activity root;
    private final GlobalStateInformation globalStateInformation;

    public ActivityTree() {
        objectivesGlobalToSystem = true;
        globalStateInformation = new GlobalStateInformation();
    }

    public boolean isObjectivesGlobalToSystem() {
        return objectivesGlobalToSystem;
    }

    public void setObjectivesGlobalToSystem(boolean objectivesGlobalToSystem) {
        this.objectivesGlobalToSystem = objectivesGlobalToSystem;
    }

    public Activity getRoot() {
        return root;
    }

    public void setRoot(Activity root) {
        this.root = root;
    }

    public GlobalStateInformation getGlobalStateInformation() {
        return globalStateInformation;
    }
}
