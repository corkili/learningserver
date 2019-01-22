package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.LocalizedString;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;

public class LearnerName implements TerminalDataType {

    private LocalizedString learnerName;

    public LearnerName() {
        this.learnerName = new LocalizedString();
    }

    @Override
    public void set(String value) {
        this.learnerName.set(value);
    }

    @Override
    public String get() {
        return this.learnerName.get();
    }

    public LocalizedString getLearnerName() {
        return learnerName;
    }

    public void setLearnerName(LocalizedString learnerName) {
        this.learnerName = learnerName;
    }
}
