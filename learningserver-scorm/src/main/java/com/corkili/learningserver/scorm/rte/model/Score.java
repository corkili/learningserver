package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.annotation.Meta;
import com.corkili.learningserver.scorm.rte.model.datatype.CharacterString;
import com.corkili.learningserver.scorm.rte.model.datatype.GeneralDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.Real7;
import com.corkili.learningserver.scorm.rte.model.datatype.Real7WithRange;

public class Score implements GeneralDataType {

    @Meta(value = "_children", writable = false)
    private CharacterString children;

    @Meta("scaled")
    private Real7WithRange scaled;

    @Meta("raw")
    private Real7 raw;

    @Meta("max")
    private Real7 max;

    @Meta("min")
    private Real7 min;

    public Score() {
        this.children = new CharacterString("scaled,min,max,raw");
        this.scaled = new Real7WithRange(-1, 1);
        this.raw = new Real7();
        this.max = new Real7();
        this.min = new Real7();
    }

    public CharacterString getChildren() {
        return children;
    }

    public void setChildren(CharacterString children) {
        this.children = children;
    }

    public Real7WithRange getScaled() {
        return scaled;
    }

    public void setScaled(Real7WithRange scaled) {
        this.scaled = scaled;
    }

    public Real7 getRaw() {
        return raw;
    }

    public void setRaw(Real7 raw) {
        this.raw = raw;
    }

    public Real7 getMax() {
        return max;
    }

    public void setMax(Real7 max) {
        this.max = max;
    }

    public Real7 getMin() {
        return min;
    }

    public void setMin(Real7 min) {
        this.min = min;
    }
}
