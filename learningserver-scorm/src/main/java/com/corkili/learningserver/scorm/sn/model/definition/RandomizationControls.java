package com.corkili.learningserver.scorm.sn.model.definition;

import com.corkili.learningserver.scorm.sn.model.datatype.Vocabulary;

/**
 * @see com.corkili.learningserver.scorm.cam.model.RandomizationControls
 */
public class RandomizationControls implements DefinitionElementSet {

    private final Vocabulary randomizationTiming;
    private boolean randomizeChildren;

    public RandomizationControls() {
        randomizationTiming = new Vocabulary("Never", "Never", "Once", "On Each New Attempt");
        randomizeChildren = false;
    }

    public Vocabulary getRandomizationTiming() {
        return randomizationTiming;
    }

    public boolean isRandomizeChildren() {
        return randomizeChildren;
    }

    public void setRandomizeChildren(boolean randomizeChildren) {
        this.randomizeChildren = randomizeChildren;
    }
}
