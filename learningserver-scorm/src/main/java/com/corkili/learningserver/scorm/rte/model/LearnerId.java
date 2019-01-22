package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.LongIdentifier;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;

public class LearnerId implements TerminalDataType {

    private LongIdentifier learnerId;

    public LearnerId() {
        this.learnerId = new LongIdentifier();
    }

    @Override
    public void set(String value) {
        this.learnerId.set(value);
    }

    @Override
    public String get() {
        return this.learnerId.get();
    }

    public LongIdentifier getLearnerId() {
        return learnerId;
    }

    public void setLearnerId(LongIdentifier learnerId) {
        this.learnerId = learnerId;
    }
}
