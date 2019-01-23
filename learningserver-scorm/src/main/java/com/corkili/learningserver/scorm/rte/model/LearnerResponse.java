package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.Interactions.Instance;
import com.corkili.learningserver.scorm.rte.model.datatype.AbstractTerminalDataType;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

// TODO 4.2.9.2 page 133
public class LearnerResponse extends AbstractTerminalDataType {

    private String learnerResponse;

    private Interactions.Instance container;

    public LearnerResponse(Instance container) {
        this.container = container;
    }

    @Override
    public ScormResult set(String value) {
        ScormResult scormResult = super.handleSet(this, value);
        if (!scormResult.getError().equals(ScormError.E_0)) {
            return scormResult;
        }
        this.learnerResponse = value;
        return scormResult;
    }

    @Override
    public ScormResult get() {
        ScormResult scormResult = super.handleGet(this);
        if (!scormResult.getError().equals(ScormError.E_0)) {
            return scormResult;
        }
        return scormResult.setReturnValue(learnerResponse);
    }

    public String getLearnerResponse() {
        return learnerResponse;
    }

    public void setLearnerResponse(String learnerResponse) {
        this.learnerResponse = learnerResponse;
    }
}
