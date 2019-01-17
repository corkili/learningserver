package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

public class SequencingCollection {

    private List<Sequencing> sequencingList; // 1...n

    public SequencingCollection() {
        sequencingList = new ArrayList<>();
    }

    public List<Sequencing> getSequencingList() {
        return sequencingList;
    }

    public void setSequencingList(List<Sequencing> sequencingList) {
        this.sequencingList = sequencingList;
    }
}
