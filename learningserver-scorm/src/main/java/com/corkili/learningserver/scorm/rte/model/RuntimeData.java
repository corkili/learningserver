package com.corkili.learningserver.scorm.rte.model;

import com.corkili.learningserver.scorm.rte.model.annotation.Meta;
import com.corkili.learningserver.scorm.rte.model.datatype.GeneralDataType;

public class RuntimeData implements GeneralDataType {

    @Meta("cmi")
    private CMI cmi;

    @Meta("adl")
    private ADL adl;

    public RuntimeData() {
        this.cmi = new CMI();
        this.adl = new ADL();
    }

    public CMI getCmi() {
        return cmi;
    }

    public void setCmi(CMI cmi) {
        this.cmi = cmi;
    }

    public ADL getAdl() {
        return adl;
    }

    public void setAdl(ADL adl) {
        this.adl = adl;
    }
}
