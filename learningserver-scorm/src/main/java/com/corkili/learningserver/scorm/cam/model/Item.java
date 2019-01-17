package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

import com.corkili.learningserver.scorm.cam.model.datatype.ID;

public class Item {

    // attributes
    private ID identifier; // M
    private String identifierref; // O
    private boolean isvisible; // O true
    private String parameters; // O

    // elements
    private String title; // 1...1
    private List<Item> itemList; // 0...n
    private Metadata metadata; // 0...1
    private String timeLimitAction; // 0...1
    private String dataFromLMS; // 0...1
    private CompletionThreshold completionThreshold; // 0...1
    private Data data; // 0...1
    private Sequencing sequencing; // 0...1
    private Presentation presentation; // 0...1
}
