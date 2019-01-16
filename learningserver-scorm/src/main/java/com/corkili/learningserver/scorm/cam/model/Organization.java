package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

import com.corkili.learningserver.scorm.cam.model.datatype.ID;

public class Organization {

    // attributes
    private ID identifier;
    private String structure;
    private boolean objectivesGlobalToSystem;
    private boolean sharedDataGlobalToSystem;

    // elements
    private String title;
    private List<Item> itemList;
    private Metadata metadata;
    private CompletionThreshold completionThreshold;
    private Sequencing sequencing;


}
