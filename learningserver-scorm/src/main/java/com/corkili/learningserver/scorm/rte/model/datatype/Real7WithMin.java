package com.corkili.learningserver.scorm.rte.model.datatype;

import java.math.BigDecimal;

import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class Real7WithMin extends AbstractTerminalDataType {

    private BigDecimal value;
    private BigDecimal min;

    public Real7WithMin(double min) {
        this.min = new BigDecimal(min).setScale(7, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public ScormResult set(String value) {
        ScormResult scormResult = super.handleSet(this, value);
        if (!scormResult.getError().equals(ScormError.E_0)) {
            return scormResult;
        }
        try {
            BigDecimal num = new BigDecimal(value).setScale(7, BigDecimal.ROUND_HALF_UP);
            if (min.compareTo(num) <= 0) {
                this.value = num;
                return scormResult;
            } else {
                return new ScormResult("false", ScormError.E_407,
                        "parameter should be a decimal, with range [" + min.toString() + ", ...)");
            }
        } catch (Exception e) {
            return new ScormResult("false", ScormError.E_406,
                    "parameter should be a decimal, with range [" + min.toString() + ", ...)");
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

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }
}
