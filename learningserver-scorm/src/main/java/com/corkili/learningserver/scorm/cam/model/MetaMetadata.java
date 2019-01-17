package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

public class MetaMetadata {

    private List<Identifier> identifierList; // 0...n
    private List<Contribute> contribute; // 0...n
    private List<String> metadataSchema; // 0...n
    private String language; // 0...1

}
