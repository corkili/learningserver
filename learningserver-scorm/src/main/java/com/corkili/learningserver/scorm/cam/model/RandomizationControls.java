package com.corkili.learningserver.scorm.cam.model;

import com.corkili.learningserver.scorm.cam.model.datatype.NonNegativeInteger;
import com.corkili.learningserver.scorm.cam.model.datatype.Token;

public class RandomizationControls {

    // attributes
    private Token randomizationTiming; // O never
    private NonNegativeInteger selectCount; // O
    private boolean reorderChildren; // O false
    private Token selectionTiming; // O never

    public RandomizationControls() {
        randomizationTiming = new Token("never");
        reorderChildren = false;
        selectionTiming = new Token("never");
    }

    public Token getRandomizationTiming() {
        return randomizationTiming;
    }

    public void setRandomizationTiming(Token randomizationTiming) {
        this.randomizationTiming = randomizationTiming;
    }

    public NonNegativeInteger getSelectCount() {
        return selectCount;
    }

    public void setSelectCount(NonNegativeInteger selectCount) {
        this.selectCount = selectCount;
    }

    public boolean isReorderChildren() {
        return reorderChildren;
    }

    public void setReorderChildren(boolean reorderChildren) {
        this.reorderChildren = reorderChildren;
    }

    public Token getSelectionTiming() {
        return selectionTiming;
    }

    public void setSelectionTiming(Token selectionTiming) {
        this.selectionTiming = selectionTiming;
    }
}
