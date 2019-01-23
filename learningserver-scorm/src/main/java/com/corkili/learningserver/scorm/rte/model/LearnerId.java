package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.LongIdentifier;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.handler.ReadOnlyHandler;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class LearnerId implements TerminalDataType {

    private LongIdentifier learnerId;

    public LearnerId() {
        this.learnerId = new LongIdentifier();
        registerHandler();
    }

    private void registerHandler() {
        learnerId.registerSetHandler(new ReadOnlyHandler());
    }

    @Override
    public ScormResult set(String value) {
        return this.learnerId.set(value);
    }

    @Override
    public ScormResult get() {
        return this.learnerId.get();
    }

    public LongIdentifier getLearnerId() {
        return learnerId;
    }

    public void setLearnerId(LongIdentifier learnerId) {
        this.learnerId = learnerId;
    }
}
