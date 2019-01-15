package com.corkili.learningserver.scorm.common;

import java.math.BigDecimal;

public class DecimalRangeLimit implements Limit<String> {

    private int scale;
    private BigDecimal min;
    private BigDecimal max;

    public DecimalRangeLimit(int scale, double minimum, double maximum) {
        this.scale = scale;
        this.min = new BigDecimal(minimum).setScale(scale, BigDecimal.ROUND_HALF_UP);
        this.max = new BigDecimal(maximum).setScale(scale, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public boolean conform(String data) {
        BigDecimal num = BigDecimal.valueOf(Double.valueOf(data))
                .setScale(scale, BigDecimal.ROUND_HALF_UP);
        return min.compareTo(num) <= 0 && num.compareTo(max) <= 0;
    }
}
