package com.corkili.learningserver.scorm.rte.model.datatype;

import org.apache.commons.lang3.StringUtils;

import com.corkili.learningserver.scorm.common.CommonUtils;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class TimeInterval extends AbstractTerminalDataType {

    private String value;

    public TimeInterval() {
    }

    public TimeInterval(String value) {
        set0(value);
    }

    @Override
    public ScormResult set(String value) {
        ScormResult scormResult = super.handleSet(this, value);
        if (!scormResult.getError().equals(ScormError.E_0)) {
            return scormResult;
        }
        if (!set0(value)) {
            return new ScormResult("false", ScormError.E_406);
        }
        return scormResult;
    }

    @Override
    public ScormResult get() {
        ScormResult scormResult = super.handleGet(this);
        if (!scormResult.getError().equals(ScormError.E_0)) {
            return scormResult;
        }
        return scormResult.setReturnValue(value);
    }

    private boolean set0(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        if (CommonUtils.isLegalDuration(value)) {
            this.value = value;
            return true;
        } else {
            return false;
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
