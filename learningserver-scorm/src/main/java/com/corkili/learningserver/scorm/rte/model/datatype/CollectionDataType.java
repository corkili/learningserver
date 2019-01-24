package com.corkili.learningserver.scorm.rte.model.datatype;

import com.corkili.learningserver.scorm.rte.model.result.CollectionScormResult;

public interface CollectionDataType<Instance> {

    CollectionScormResult<Instance> get(int index);

    CollectionScormResult<Instance> set(int index);

    void syncNewInstance(boolean operatorIsSuccess);

}
