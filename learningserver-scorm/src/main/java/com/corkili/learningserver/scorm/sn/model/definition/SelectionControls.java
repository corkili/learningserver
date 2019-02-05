package com.corkili.learningserver.scorm.sn.model.definition;

import com.corkili.learningserver.scorm.sn.model.datatype.NonNegativeInteger;
import com.corkili.learningserver.scorm.sn.model.datatype.Vocabulary;

/**
 * @see com.corkili.learningserver.scorm.cam.model.RandomizationControls
 */
public class SelectionControls implements DefinitionElementSet {

    private final Vocabulary selectionTiming;
    private boolean selectionCountStatus;
    private final NonNegativeInteger selectionCount;

    public SelectionControls() {
        selectionTiming = new Vocabulary("Never", "Never", "Once", "On Each New Attempt");
        selectionCountStatus = false;
        selectionCount = new NonNegativeInteger(0);
    }

    public Vocabulary getSelectionTiming() {
        return selectionTiming;
    }

    public boolean isSelectionCountStatus() {
        return selectionCountStatus;
    }

    public void setSelectionCountStatus(boolean selectionCountStatus) {
        this.selectionCountStatus = selectionCountStatus;
    }

    public NonNegativeInteger getSelectionCount() {
        return selectionCount;
    }
}
