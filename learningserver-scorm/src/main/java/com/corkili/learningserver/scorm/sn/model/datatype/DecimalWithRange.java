package com.corkili.learningserver.scorm.sn.model.datatype;

import java.math.BigDecimal;

public class DecimalWithRange implements Comparable<DecimalWithRange> {

    private BigDecimal value;
    private final BigDecimal min;
    private final BigDecimal max;
    private final int scale;

    public DecimalWithRange(double value, double min, double max, int scale) {
        this(min, max, scale);
        setValue(value);
    }

    public DecimalWithRange(double min, double max, int scale) {
        this.scale = scale;
        this.min = new BigDecimal(min).setScale(scale, BigDecimal.ROUND_HALF_UP);
        this.max = new BigDecimal(max).setScale(scale, BigDecimal.ROUND_HALF_UP);
        if (this.min.compareTo(this.max) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(double value) {
        BigDecimal newValue = new BigDecimal(value).setScale(scale, BigDecimal.ROUND_HALF_UP);
        if (min.compareTo(newValue) <= 0 && newValue.compareTo(max) <= 0) {
            this.value = newValue;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal getMin() {
        return min;
    }

    public BigDecimal getMax() {
        return max;
    }

    public int getScale() {
        return scale;
    }

    @Override
    public int compareTo(DecimalWithRange that) {
        return this.value.compareTo(that.value);
    }
}
