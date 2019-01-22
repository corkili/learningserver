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

public class Objectives extends AbstractCollectionDataType<Objectives.Instance> {

    @Meta(value = "_children", writable = false)
    private CharacterString children;

    @Meta(value = "_count", writable = false)
    private Int count;

    public Objectives() {
        this.children = new CharacterString("id,score,success_status,completion_status,progress_measure,description");
        this.count = new Int(0);
    }

    @Override
    protected Instance newInstance() {
        count.set(String.valueOf(Integer.parseInt(count.get()) + 1));
        return new Instance();
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

        public Instance() {
            this.id = new LongIdentifier();
            this.score = new ObjectivesScore();
            this.successStatus = new State(new String[]{"passed", "failed", "unknown"});
            this.completionStatus = new State(new String[]{"completed", "incomplete", "not_attempted", "unknown"});
            this.progressMeasure = new Real7WithRange(0, 1);
            this.description = new LocalizedString();
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
