package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.annotation.Meta;
import com.corkili.learningserver.scorm.rte.model.datatype.AbstractCollectionDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.CharacterString;
import com.corkili.learningserver.scorm.rte.model.datatype.GeneralDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.Int;
import com.corkili.learningserver.scorm.rte.model.datatype.LocalizedString;
import com.corkili.learningserver.scorm.rte.model.datatype.LongIdentifier;
import com.corkili.learningserver.scorm.rte.model.datatype.Real7WithRange;
import com.corkili.learningserver.scorm.rte.model.datatype.State;
import com.corkili.learningserver.scorm.rte.model.error.Diagnostic;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.handler.ReadOnlyHandler;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class Objectives extends AbstractCollectionDataType<Objectives.Instance> {

    @Meta(value = "_children", writable = false)
    private CharacterString children;

    @Meta(value = "_count", writable = false)
    private Int count;

    public Objectives() {
        this.children = new CharacterString("id,score,success_status,completion_status,progress_measure,description");
        this.count = new Int(0);
        registerHandler();
    }

    private void registerHandler() {
        children.registerSetHandler(new ReadOnlyHandler());
        count.registerSetHandler(new ReadOnlyHandler());
    }

    @Override
    protected Instance newInstance() {
        return new Instance(this);
    }

    @Override
    protected void addCount() {
        count.setValue(count.getValue() + 1);
    }

    public CharacterString getChildren() {
        return children;
    }

    public void setChildren(CharacterString children) {
        this.children = children;
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

        @Meta("score")
        private ObjectivesScore score;

        @Meta("success_status")
        private State successStatus;

        @Meta("completion_status")
        private State completionStatus;

        @Meta("progress_measure")
        private Real7WithRange progressMeasure;

        @Meta("description")
        private LocalizedString description;

        private Objectives container;

        public Instance(Objectives container) {
            this.id = new LongIdentifier();
            this.score = new ObjectivesScore(this);
            this.successStatus = new State(new String[]{"passed", "failed", "unknown"});
            this.successStatus.setValue("unknown");
            this.completionStatus = new State(new String[]{"completed", "incomplete", "not_attempted", "unknown"});
            this.completionStatus.setValue("unknown");
            this.progressMeasure = new Real7WithRange(0, 1);
            this.description = new LocalizedString();
            this.container = container;
            registerHandler();
        }

        private void registerHandler() {
            id.registerSetHandler((context, value) -> {
                String nowValue = ((LongIdentifier) context).getValue();
                if (nowValue != null) {
                    if (nowValue.equals(value)) {
                        return new ScormResult("true", ScormError.E_0);
                    } else {
                        return new ScormResult("false", ScormError.E_351,
                                Diagnostic.IDENTIFIER_VALUE_CAN_ONLY_BE_SET_ONCE);
                    }
                }
                boolean isUnique = true;
                for (Instance instance : container.getInstances()) {
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

            successStatus.registerSetHandler((context, value) -> {
                if (id.getValue() == null) {
                    return new ScormResult("false", ScormError.E_408);
                }
                return new ScormResult("true", ScormError.E_0);
            });

            completionStatus.registerSetHandler((context, value) -> {
                if (id.getValue() == null) {
                    return new ScormResult("false", ScormError.E_408);
                }
                return new ScormResult("true", ScormError.E_0);
            });

            progressMeasure.registerGetHandler(context -> {
                if (((Real7WithRange) context).getValue() == null) {
                    return new ScormResult("", ScormError.E_403);
                }
                return new ScormResult("", ScormError.E_0);
            }).registerSetHandler((context, value) -> {
                if (id.getValue() == null) {
                    return new ScormResult("false", ScormError.E_408);
                }
                return new ScormResult("true", ScormError.E_0);
            });

            description.registerGetHandler(context -> {
                if (((LocalizedString) context).getValue() == null) {
                    return new ScormResult("", ScormError.E_403);
                }
                return new ScormResult("", ScormError.E_0);
            }).registerSetHandler((context, value) -> {
                if (id.getValue() == null) {
                    return new ScormResult("false", ScormError.E_408);
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

        public ObjectivesScore getScore() {
            return score;
        }

        public void setScore(ObjectivesScore score) {
            this.score = score;
        }

        public State getSuccessStatus() {
            return successStatus;
        }

        public void setSuccessStatus(State successStatus) {
            this.successStatus = successStatus;
        }

        public State getCompletionStatus() {
            return completionStatus;
        }

        public void setCompletionStatus(State completionStatus) {
            this.completionStatus = completionStatus;
        }

        public Real7WithRange getProgressMeasure() {
            return progressMeasure;
        }

        public void setProgressMeasure(Real7WithRange progressMeasure) {
            this.progressMeasure = progressMeasure;
        }

        public LocalizedString getDescription() {
            return description;
        }

        public void setDescription(LocalizedString description) {
            this.description = description;
        }
    }


}
