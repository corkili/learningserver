package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;

public class LearnerResponse implements TerminalDataType {

    private String learnerResponse;

    @Override
    public void set(String value) {
        this.learnerResponse = value;
    }

    @Override
    public String get() {
        return this.learnerResponse;
    }

    public String getLearnerResponse() {
        return learnerResponse;
    }

    public void setLearnerResponse(String learnerResponse) {
        this.learnerResponse = learnerResponse;
    }
}
