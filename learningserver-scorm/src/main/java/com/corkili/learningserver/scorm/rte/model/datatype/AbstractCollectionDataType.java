package com.corkili.learningserver.scorm.rte.model.datatype;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCollectionDataType<Instance> implements CollectionDataType<Instance> {

    private List<Instance> instances;

    public AbstractCollectionDataType() {
        instances = new ArrayList<>();
    }

    @Override
    public final Instance get(int index) {
        if (index > instances.size()) {
            throw new IndexOutOfBoundsException();
        } else if (index == instances.size()) {
            instances.add(newInstance());
        }
        return instances.get(index);
    }

    protected abstract Instance newInstance();

    public List<Instance> getInstances() {
        return instances;
    }

    public void setInstances(List<Instance> instances) {
        this.instances = instances;
    }
}
