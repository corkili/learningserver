package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.annotation.Meta;
import com.corkili.learningserver.scorm.rte.model.datatype.AbstractCollectionDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.GeneralDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.Int;

public class CorrectResponses extends AbstractCollectionDataType<CorrectResponses.Instance> {

    @Meta(value = "_count", writable = false)
    private Int count;

    public CorrectResponses() {
        this.count = new Int(0);
    }

    @Override
    protected Instance newInstance() {
        count.set(String.valueOf(Integer.parseInt(count.get()) + 1));
        return new Instance();
    }

    public static class Instance implements GeneralDataType {

        @Meta("pattern")
        private Pattern pattern;

        public Instance() {
            this.pattern = new Pattern();
        }
    }

    public Int getCount() {
        return count;
    }

    public void setCount(Int count) {
        this.count = count;
    }
}
