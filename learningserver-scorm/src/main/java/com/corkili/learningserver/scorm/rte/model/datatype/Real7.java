package com.corkili.learningserver.scorm.rte.model.datatype;

import java.math.BigDecimal;

import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class Real7 extends AbstractTerminalDataType {

    private BigDecimal value;

    public Real7() {
    }

    public Real7(double value) {
        this(String.valueOf(value));
    }

    public Real7(String value) {
        set(value);
    }

    @Override
    public ScormResult set(String value) {
        ScormResult scormResult = super.handleSet(this, value);
        if (!scormResult.getError().equals(ScormError.E_0)) {
            return scormResult;
        }
        try {
            this.value = BigDecimal.valueOf(Double.parseDouble(value))
                    .setScale(7, BigDecimal.ROUND_HALF_UP);
            return scormResult;
        } catch (Exception e) {
            return new ScormResult("false", ScormError.E_406, "parameter should be a decimal");
        }
    }

    @Override
    public ScormResult get() {
        ScormResult scormResult = super.handleGet(this);
        if (!scormResult.getError().equals(ScormError.E_0)) {
            return scormResult;
        }
        return scormResult.setReturnValue(value.toString());
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
