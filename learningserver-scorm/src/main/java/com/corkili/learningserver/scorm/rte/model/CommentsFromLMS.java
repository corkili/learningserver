package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.annotation.Meta;
import com.corkili.learningserver.scorm.rte.model.datatype.AbstractCollectionDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.CharacterString;
import com.corkili.learningserver.scorm.rte.model.datatype.GeneralDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.Int;
import com.corkili.learningserver.scorm.rte.model.datatype.LocalizedString;
import com.corkili.learningserver.scorm.rte.model.datatype.Time;

public class CommentsFromLMS extends AbstractCollectionDataType<CommentsFromLMS.Instance> {

    @Meta("_children")
    private CharacterString children;

    @Meta("_count")
    private Int count;

    public CommentsFromLMS() {
        children = new CharacterString("comment,location,timestamp");
        count = new Int(0);
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

        @Meta(value = "comment", writable = false)
        private LocalizedString comment;

        @Meta(value = "location", writable = false)
        private CharacterString location;

        @Meta(value = "timestamp", writable = false)
        private Time timestamp;

        public Instance() {
            this.comment = new LocalizedString();
            this.location = new CharacterString();
            this.timestamp = new Time();
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
