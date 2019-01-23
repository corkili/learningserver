package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.annotation.Meta;
import com.corkili.learningserver.scorm.rte.model.datatype.AbstractCollectionDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.CharacterString;
import com.corkili.learningserver.scorm.rte.model.datatype.GeneralDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.Int;
import com.corkili.learningserver.scorm.rte.model.datatype.LongIdentifier;
import com.corkili.learningserver.scorm.rte.model.handler.ReadOnlyHandler;

public class Data extends AbstractCollectionDataType<Data.Instance> {

    @Meta(value = "_children", writable = false)
    private CharacterString children;

    @Meta(value = "_count", writable = false)
    private Int count;

    public Data() {
        this.children = new CharacterString("id,store");
        this.count = new Int(0);
        registerHandler();
    }

    private void registerHandler() {
        children.registerSetHandler(new ReadOnlyHandler());
        count.registerSetHandler(new ReadOnlyHandler());
    }

    @Override
    protected Instance newInstance() {
        return new Instance();
    }

    @Override
    protected boolean hasReadOnlyElement() {
        return true;
    }

    public static class Instance implements GeneralDataType {

        // TODO
        @Meta(value = "id", writable = false)
        private LongIdentifier id;

        @Meta("store")
        private CharacterString store;

        public Instance() {
            this.id = new LongIdentifier();
            this.store = new CharacterString();
            registerHandler();
        }

        private void registerHandler() {
            id.registerSetHandler(new ReadOnlyHandler());
        }
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
}
