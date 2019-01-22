package com.corkili.learningserver.scorm.cam.model.datatype;

import java.math.BigDecimal;

public class Decimal extends XMLDataType {

    private BigDecimal decimalValue;
    private int scale;

    public Decimal(String value, int scale) {
        super(value);
        this.scale = scale;
        decimalValue = BigDecimal.valueOf(Double.valueOf(value))
                .setScale(this.scale, BigDecimal.ROUND_HALF_UP);
    }
}
