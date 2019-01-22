package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.annotation.Meta;
import com.corkili.learningserver.scorm.rte.model.datatype.AbstractCollectionDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.GeneralDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.Int;
import com.corkili.learningserver.scorm.rte.model.datatype.LongIdentifier;

public class InteractionsObjectives extends AbstractCollectionDataType<InteractionsObjectives.Instance> {

    @Meta(value = "_count", writable = false)
    private Int count;

    public InteractionsObjectives() {
        this.count = new Int(0);
    }

    @Override
    protected Instance newInstance() {
        count.set(String.valueOf(Integer.parseInt(count.get()) + 1));
        return new Instance();
    }

    public Int getCount() {
        return count;
    }

    public void setCount(Int count) {
        this.count = count;
    }

    public static class Instance implements GeneralDataType {

        @Meta("id")
        private LongIdentifier id;

        public Instance() {
            this.id = new LongIdentifier();
        }

        public LongIdentifier getId() {
            return id;
        }

        public void setId(LongIdentifier id) {
            this.id = id;
        }
    }

}
