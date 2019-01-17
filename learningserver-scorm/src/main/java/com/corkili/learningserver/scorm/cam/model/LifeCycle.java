package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

public class LifeCycle {

    private LanguageString version; // 0...1
    private Vocabulary status; // 0...1
    private List<Contribute> contributeList; // 0...n

    public LifeCycle() {
        contributeList = new ArrayList<>();
    }

    public LanguageString getVersion() {
        return version;
    }

    public void setVersion(LanguageString version) {
        this.version = version;
    }

    public Vocabulary getStatus() {
        return status;
    }

    public void setStatus(Vocabulary status) {
        this.status = status;
    }

    public List<Contribute> getContributeList() {
        return contributeList;
    }

    public void setContributeList(List<Contribute> contributeList) {
        this.contributeList = contributeList;
    }
}
