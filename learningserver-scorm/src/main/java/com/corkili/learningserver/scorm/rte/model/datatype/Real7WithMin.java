package com.corkili.learningserver.scorm.rte.model.datatype;

import java.math.BigDecimal;

public class Real7WithMin implements TerminalDataType{

    private Real7 value;
    private BigDecimal min;

    public Real7WithMin(double min) {
        this.value = new Real7();
        this.min = new BigDecimal(min).setScale(7, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public void set(String value) {
        BigDecimal num = new BigDecimal(value).setScale(7, BigDecimal.ROUND_HALF_UP);
        if (min.compareTo(num) <= 0) {
            this.value.set(value);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String get() {
        return value.get();
    }

    public Real7 getValue() {
        return value;
    }

    public void setValue(Real7 value) {
        this.value = value;
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }
}
