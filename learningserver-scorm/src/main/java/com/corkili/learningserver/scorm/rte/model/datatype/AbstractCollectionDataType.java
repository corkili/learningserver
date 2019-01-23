package com.corkili.learningserver.scorm.rte.model.datatype;

import java.util.ArrayList;
import java.util.List;

import com.corkili.learningserver.scorm.rte.model.error.Diagnostic;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.CollectionScormResult;

public abstract class AbstractCollectionDataType<Instance> implements CollectionDataType<Instance> {

    private List<Instance> instances;

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
        if (hasReadOnlyElement() && index >= instances.size()) {
            return new CollectionScormResult<>("false", ScormError.E_351,
                    "Data Model Element Collection Set Out Of Range");
        }
        if (index > instances.size()) {
            return new CollectionScormResult<>("false", ScormError.E_351,
                    Diagnostic.DATA_MODEL_ELEMENT_COLLECTION_SET_OUT_OF_ORDER);
        } else if (index == instances.size()) {
            instances.add(newInstance());
        }
        return new CollectionScormResult<>("true", ScormError.E_0, instances.get(index));
    }

    protected abstract Instance newInstance();

    protected boolean hasReadOnlyElement() {
        return false;
    }

    public List<Instance> getInstances() {
        return instances;
    }

    public void setInstances(List<Instance> instances) {
        this.instances = instances;
    }
}
