package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.annotation.Meta;
import com.corkili.learningserver.scorm.rte.model.datatype.AbstractCollectionDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.GeneralDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.Int;
import com.corkili.learningserver.scorm.rte.model.datatype.LongIdentifier;
import com.corkili.learningserver.scorm.rte.model.error.Diagnostic;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.handler.ReadOnlyHandler;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class InteractionsObjectives extends AbstractCollectionDataType<InteractionsObjectives.Instance> {

    @Meta(value = "_count", writable = false)
    private Int count;

    private Interactions.Instance container;

    public InteractionsObjectives(Interactions.Instance container) {
        this.count = new Int(0);
        this.container = container;
        registerHandler();
    }

    private void registerHandler() {
        count.registerSetHandler(new ReadOnlyHandler());
    }

    @Override
    protected Instance newInstance() {
        return new Instance(container);
    }

    @Override
    protected void addCount() {
        count.setValue(count.getValue() + 1);
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

        private Interactions.Instance superContainer;

        public Instance(Interactions.Instance superContainer) {
            this.id = new LongIdentifier();
            this.superContainer = superContainer;
            registerHandler();
        }

        private void registerHandler() {
            id.registerSetHandler((context, value) -> {
                if (superContainer.getId().getValue() == null) {
                    return new ScormResult("false", ScormError.E_408);
                }
                boolean isUnique = true;
                for (Instance instance : superContainer.getObjectives().getInstances()) {
                    if (instance.getId().getValue().equals(value)) {
                        isUnique = false;
                        break;
                    }
                }
                if (!isUnique) {
                    return new ScormResult("false", ScormError.E_351,
                            Diagnostic.UNIQUE_IDENTIFIER_CONSTRAINT_VIOLATED);
                }
                return new ScormResult("true", ScormError.E_0);
            });
        }

        public LongIdentifier getId() {
            return id;
        }

        public void setId(LongIdentifier id) {
            this.id = id;
        }
    }

}
