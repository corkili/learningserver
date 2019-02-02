package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.annotation.Meta;
import com.corkili.learningserver.scorm.rte.model.datatype.GeneralDataType;

public class ADL implements GeneralDataType {

    @Meta("data")
    private Data data;

    @Meta("nav")
    private Nav nav;

    public ADL() {
        this.data = new Data();
        this.nav = new Nav();
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Nav getNav() {
        return nav;
    }

    public ADL setNav(Nav nav) {
        this.nav = nav;
        return this;
    }
}
