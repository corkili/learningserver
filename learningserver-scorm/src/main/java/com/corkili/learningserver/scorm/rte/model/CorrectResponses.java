package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.annotation.Meta;
import com.corkili.learningserver.scorm.rte.model.datatype.AbstractCollectionDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.GeneralDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.Int;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.handler.ReadOnlyHandler;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class CorrectResponses extends AbstractCollectionDataType<CorrectResponses.Instance> {

    @Meta(value = "_count", writable = false)
    private Int count;

    private Interactions.Instance container;

    public CorrectResponses(Interactions.Instance container) {
        this.count = new Int(0);
        this.container = container;
        registerHandler();
    }

    private void registerHandler() {
        count.registerSetHandler(new ReadOnlyHandler());
    }

    @Override
    protected Instance newInstance() {
        count.setValue(count.getValue() + 1);
        return new Instance(container);
    }

    public static class Instance implements GeneralDataType {

        @Meta("pattern")
        private Pattern pattern;

        private Interactions.Instance superContainer;

        public Instance(Interactions.Instance superContainer) {
            this.superContainer = superContainer;
            this.pattern = new Pattern(superContainer);
            registerHandler();
        }

        private void registerHandler() {
            pattern.registerGetHandler(context -> {
                if (((Pattern) context).getPattern() == null) {
                    return new ScormResult("", ScormError.E_403);
                }
                return new ScormResult("", ScormError.E_0);
            }).registerSetHandler((context, value) -> {
                if (superContainer.getId().getValue() == null) {
                    return new ScormResult("false", ScormError.E_408);
                }
                return new ScormResult("true", ScormError.E_0);
            });
        }
    }

    public Int getCount() {
        return count;
    }

    public void setCount(Int count) {
        this.count = count;
    }
}
