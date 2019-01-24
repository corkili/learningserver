package com.corkili.learningserver.scorm.rte.model.datatype;

import com.corkili.learningserver.scorm.common.CommonUtils;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;
import com.corkili.learningserver.scorm.rte.model.util.ModelUtils;

public class LocalizedString extends AbstractTerminalDataType {

    private String language;
    private String content;
    private String value;

    public LocalizedString() {
    }

    public LocalizedString(String value) {
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
        int startIndex = value.indexOf("{");
        int endIndex = value.indexOf("}");
        if (startIndex != 0 || endIndex < 0) {
            defaultSet(value);
            return true;
        }
        String descriptor = value.substring(0, endIndex + 1);
        if (!ModelUtils.isDelimiterFormatCorrect(descriptor, "lang")) {
            defaultSet(value);
            return true;
        }
        String dv = descriptor.substring(1, descriptor.length() - 1).split("=")[1];
        if (CommonUtils.isLegalLanguage(dv)) {
            this.language = dv;
            this.content = value.substring(endIndex + 1);
            this.value = value;
            return true;
        }
        return false;
    }

    private void defaultSet(String value) {
        this.language = "en";
        this.content = value;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        set0(value);
    }

    public String getLanguage() {
        return language;
    }

    public String getContent() {
        return content;
    }

}
