package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.LocalizedString;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.handler.ReadOnlyHandler;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class LearnerName implements TerminalDataType {

    private LocalizedString learnerName;

    public LearnerName() {
        this.learnerName = new LocalizedString();
        registerHandler();
    }

    private void registerHandler() {
        learnerName.registerSetHandler(new ReadOnlyHandler());
    }

    @Override
    public ScormResult set(String value) {
        return this.learnerName.set(value);
    }

    @Override
    public ScormResult get() {
        return this.learnerName.get();
    }

    public LocalizedString getLearnerName() {
        return learnerName;
    }

    public void setLearnerName(LocalizedString learnerName) {
        this.learnerName = learnerName;
    }
}
