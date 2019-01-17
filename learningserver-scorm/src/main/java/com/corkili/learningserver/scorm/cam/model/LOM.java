package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

public class LOM {

    // elements
    private General general; // 0...1
    private LifeCycle lifeCycle; // 0...1
    private MetaMetadata metaMetadata; // 0...1
    private Technical technical; // 0...1
    private List<Educational> educationalList; // 0...n
    private Rights rights; // 0...1
    private List<Relation> relationList; // 0...n
    private List<Annotation> annotationList; // 0...n
    private List<Classification> classification; // 0...n

}
