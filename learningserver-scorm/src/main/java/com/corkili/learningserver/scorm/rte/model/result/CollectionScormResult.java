package com.corkili.learningserver.scorm.rte.model.result;

import com.sun.istack.internal.NotNull;

import com.corkili.learningserver.scorm.rte.model.error.ScormError;

public class CollectionScormResult<Instance> extends ScormResult {

    private Instance instance;

    public CollectionScormResult(@NotNull String returnValue, @NotNull ScormError error) {
        this(returnValue, error, (Instance) null);
    }

    public CollectionScormResult(@NotNull String returnValue, @NotNull ScormError error, @NotNull String diagnostic) {
        this(returnValue, error, diagnostic, (Instance) null);
    }

    public CollectionScormResult(@NotNull String returnValue, @NotNull ScormError error, Instance instance) {
        super(returnValue, error);
        this.instance = instance;
    }

    public CollectionScormResult(@NotNull String returnValue, @NotNull ScormError error,
                                 @NotNull String diagnostic, Instance instance) {
        super(returnValue, error, diagnostic);
        this.instance = instance;
    }

    public Instance getInstance() {
        return instance;
    }

    public void setInstance(Instance instance) {
        this.instance = instance;
    }
}
