package com.corkili.learningserver.scorm.rte.model.datatype;

import com.corkili.learningserver.scorm.rte.model.error.Diagnostic;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.CollectionScormResult;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCollectionDataType<Instance> implements CollectionDataType<Instance> {

    private List<Instance> instances;

    private Instance lastNewInstance;

    public AbstractCollectionDataType() {
        instances = new ArrayList<>();
    }

    @Override
    public final CollectionScormResult<Instance> get(int index) {
        if (index >= instances.size()) {
            return new CollectionScormResult<>("", ScormError.E_301,
                    Diagnostic.DATA_MODEL_COLLECTION_ELEMENT_REQUEST_OUT_OF_RANGE);
        }
        return new CollectionScormResult<>("", ScormError.E_0, instances.get(index));
    }

    @Override
    public final CollectionScormResult<Instance> set(int index) {
        Instance instance;
        if (index > instances.size()) {
            return new CollectionScormResult<>("false", ScormError.E_351,
                    Diagnostic.DATA_MODEL_ELEMENT_COLLECTION_SET_OUT_OF_ORDER);
        } else if (index == instances.size()) { // need a new instance
            instance = this.lastNewInstance = newInstance();
        } else {
            instance = instances.get(index);
        }
        return new CollectionScormResult<>("true", ScormError.E_0, instance);
    }

    @Override
    public void syncNewInstance(boolean operatorIsSuccess) {
        if (lastNewInstance == null) {
            return;
        }
        if (operatorIsSuccess) {
            instances.add(lastNewInstance);
            addCount();
        }
        lastNewInstance = null;
    }

    @Override
    public int count() {
        return instances.size();
    }

    protected abstract Instance newInstance();

    protected abstract void addCount();

    public List<Instance> getInstances() {
        return instances;
    }

    public void setInstances(List<Instance> instances) {
        this.instances = instances;
    }
}
