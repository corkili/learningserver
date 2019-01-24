package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.annotation.Meta;
import com.corkili.learningserver.scorm.rte.model.datatype.AbstractCollectionDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.CharacterString;
import com.corkili.learningserver.scorm.rte.model.datatype.GeneralDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.Int;
import com.corkili.learningserver.scorm.rte.model.datatype.LongIdentifier;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.handler.ReadOnlyHandler;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

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

        @Meta(value = "id", writable = false)
        private LongIdentifier id;

        @Meta("store")
        private CharacterString store;

        private boolean readSharedData;
        private boolean writeSharedData;

        public Instance() {
            this.id = new LongIdentifier();
            this.store = new CharacterString();
            registerHandler();
        }

        private void registerHandler() {
            id.registerSetHandler(new ReadOnlyHandler());

            store.registerGetHandler(context -> {
                if (((CharacterString) context).getValue() == null) {
                    return new ScormResult("", ScormError.E_403);
                }
                if (!readSharedData) {
                    return new ScormResult("", ScormError.E_405);
                }
                return new ScormResult("", ScormError.E_0);
            }).registerSetHandler((context, value) -> {
                if (id.getValue() == null) {
                    return new ScormResult("false", ScormError.E_408);
                }
                if (!writeSharedData) {
                    return new ScormResult("false", ScormError.E_404);
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

        public CharacterString getStore() {
            return store;
        }

        public void setStore(CharacterString store) {
            this.store = store;
        }

        public boolean isReadSharedData() {
            return readSharedData;
        }

        public void setReadSharedData(boolean readSharedData) {
            this.readSharedData = readSharedData;
        }

        public boolean isWriteSharedData() {
            return writeSharedData;
        }

        public void setWriteSharedData(boolean writeSharedData) {
            this.writeSharedData = writeSharedData;
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
