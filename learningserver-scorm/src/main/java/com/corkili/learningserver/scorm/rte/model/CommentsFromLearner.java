package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.annotation.Meta;
import com.corkili.learningserver.scorm.rte.model.datatype.AbstractCollectionDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.CharacterString;
import com.corkili.learningserver.scorm.rte.model.datatype.GeneralDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.Int;
import com.corkili.learningserver.scorm.rte.model.datatype.LocalizedString;
import com.corkili.learningserver.scorm.rte.model.datatype.Time;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.handler.ReadOnlyHandler;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

public class CommentsFromLearner extends AbstractCollectionDataType<CommentsFromLearner.Instance> {

    @Meta(value = "_children", writable = false)
    private CharacterString children;

    @Meta(value = "_count", writable = false)
    private Int count;

    public CommentsFromLearner() {
        children = new CharacterString("comment,location,timestamp");
        count = new Int(0);
        registerHandler();
    }

    @Override
    protected Instance newInstance() {
        count.setValue(count.getValue() + 1);
        return new Instance();
    }

    private void registerHandler() {
        children.registerSetHandler(new ReadOnlyHandler());
        count.registerSetHandler(new ReadOnlyHandler());
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

        @Meta("comment")
        private LocalizedString comment;

        @Meta("location")
        private CharacterString location;

        @Meta("timestamp")
        private Time timestamp;

        public Instance() {
            this.comment = new LocalizedString();
            this.location = new CharacterString();
            this.timestamp = new Time();
            registerHandler();
        }

        private void registerHandler() {
            comment.registerGetHandler(context -> {
               LocalizedString c = (LocalizedString) context;
               if (c.getValue() == null) {
                   return new ScormResult("", ScormError.E_403);
               }
               return new ScormResult("", ScormError.E_0);
            });

            location.registerGetHandler(context -> {
                CharacterString l = (CharacterString) context;
                if (l.getValue() == null) {
                    return new ScormResult("", ScormError.E_403);
                }
                return new ScormResult("", ScormError.E_0);
            });

            timestamp.registerGetHandler(context -> {
                Time t = (Time) context;
                if (t.getValue() == null) {
                    return new ScormResult("", ScormError.E_403);
                }
                return new ScormResult("", ScormError.E_0);
            });

        }

        public LocalizedString getComment() {
            return comment;
        }

        public void setComment(LocalizedString comment) {
            this.comment = comment;
        }

        public CharacterString getLocation() {
            return location;
        }

        public void setLocation(CharacterString location) {
            this.location = location;
        }

        public Time getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Time timestamp) {
            this.timestamp = timestamp;
        }
    }

}
