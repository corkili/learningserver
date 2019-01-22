package com.corkili.learningserver.scorm.rte.model.datatype;

import java.math.BigDecimal;

public class Real7 implements TerminalDataType {

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
    public void set(String value) {
        this.value = BigDecimal.valueOf(Double.parseDouble(value))
                .setScale(7, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public String get() {
        return value.toString();
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
