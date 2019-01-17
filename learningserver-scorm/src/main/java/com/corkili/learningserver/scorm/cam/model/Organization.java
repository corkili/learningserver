package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

import com.corkili.learningserver.scorm.cam.model.datatype.ID;

public class Organization {

    // attributes
    private ID identifier; // M
    private String structure; // O hierarchical
    private boolean objectivesGlobalToSystem; // O true
    private boolean sharedDataGlobalToSystem; // O true

    // elements
    private String title; // 1...1
    private List<Item> itemList; // 1...n
    private Metadata metadata; // 0..1
    private CompletionThreshold completionThreshold; // 0...1
    private Sequencing sequencing; // 0...1


}
