package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

import com.corkili.learningserver.scorm.cam.model.datatype.ID;

public class Item {

    // attributes
    private ID identifier;
    private String identifierref;
    private boolean isvisible;
    private String parameters;

    // elements
    private String title;
    private List<Item> itemList;
    private Metadata metadata;
    private String timeLimitAction;
    private String dataFromLMS;
    private CompletionThreshold completionThreshold;
    private Data data;
    private Sequencing sequencing;
    private Presentation presentation;
}
