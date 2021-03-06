package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.annotation.Meta;
import com.corkili.learningserver.scorm.rte.model.datatype.CharacterString;
import com.corkili.learningserver.scorm.rte.model.datatype.GeneralDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.Real7;
import com.corkili.learningserver.scorm.rte.model.datatype.Real7WithRange;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.handler.ReadOnlyHandler;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class ObjectivesScore implements GeneralDataType {

    @Meta(value = "_children", writable = false)
    private CharacterString children;

    @Meta("scaled")
    private Real7WithRange scaled;

    @Meta("raw")
    private Real7 raw;

    @Meta("min")
    private Real7 min;

    @Meta("max")
    private Real7 max;

    private Objectives.Instance container;

    public ObjectivesScore(Objectives.Instance container) {
        this.children = new CharacterString("scaled,raw,min,max");
        this.scaled = new Real7WithRange(-1, 1);
        this.raw = new Real7();
        this.min = new Real7();
        this.max = new Real7();
        this.container = container;
        registerHandler();
    }

    private void registerHandler() {
        children.registerSetHandler(new ReadOnlyHandler());

        scaled.registerGetHandler(context -> {
           if (((Real7WithRange) context).getValue() == null) {
               return new ScormResult("", ScormError.E_403);
           }
           return new ScormResult("", ScormError.E_0);
        }).registerSetHandler((context, value) -> {
            if (container.getId().getValue() == null) {
                return new ScormResult("false", ScormError.E_408);
            }
            return new ScormResult("true", ScormError.E_0);
        });

        raw.registerGetHandler(context -> {
            if (((Real7) context).getValue() == null) {
                return new ScormResult("", ScormError.E_403);
            }
            return new ScormResult("", ScormError.E_0);
        }).registerSetHandler((context, value) -> {
            if (container.getId().getValue() == null) {
                return new ScormResult("false", ScormError.E_408);
            }
            return new ScormResult("true", ScormError.E_0);
        });

        min.registerGetHandler(context -> {
            if (((Real7) context).getValue() == null) {
                return new ScormResult("", ScormError.E_403);
            }
            return new ScormResult("", ScormError.E_0);
        }).registerSetHandler((context, value) -> {
            if (container.getId().getValue() == null) {
                return new ScormResult("false", ScormError.E_408);
            }
            return new ScormResult("true", ScormError.E_0);
        });

        max.registerGetHandler(context -> {
            if (((Real7) context).getValue() == null) {
                return new ScormResult("", ScormError.E_403);
            }
            return new ScormResult("", ScormError.E_0);
        }).registerSetHandler((context, value) -> {
            if (container.getId().getValue() == null) {
                return new ScormResult("false", ScormError.E_408);
            }
            return new ScormResult("true", ScormError.E_0);
        });

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

    public Real7 getMin() {
        return min;
    }

    public void setMin(Real7 min) {
        this.min = min;
    }

    public Real7 getMax() {
        return max;
    }

    public void setMax(Real7 max) {
        this.max = max;
    }
}
