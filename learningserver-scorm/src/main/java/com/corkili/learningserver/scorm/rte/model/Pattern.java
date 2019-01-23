package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.Interactions.Instance;
import com.corkili.learningserver.scorm.rte.model.datatype.AbstractTerminalDataType;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

// TODO 4.2.9.1 page 124
public class Pattern extends AbstractTerminalDataType {

    private String pattern;

    private Interactions.Instance superSuperContainer;

    public Pattern(Instance superSuperContainer) {
        this.superSuperContainer = superSuperContainer;
    }

    @Override
    public ScormResult set(String value) {
        ScormResult scormResult = super.handleSet(this, value);
        if (!scormResult.getError().equals(ScormError.E_0)) {
            return scormResult;
        }
        this.pattern = value;
        return scormResult;
    }

    @Override
    public ScormResult get() {
        ScormResult scormResult = super.handleGet(this);
        if (!scormResult.getError().equals(ScormError.E_0)) {
            return scormResult;
        }
        return scormResult.setReturnValue(pattern);
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
