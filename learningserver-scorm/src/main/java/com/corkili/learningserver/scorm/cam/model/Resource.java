package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

import com.corkili.learningserver.scorm.cam.model.datatype.AnyURI;
import com.corkili.learningserver.scorm.cam.model.datatype.File;
import com.corkili.learningserver.scorm.cam.model.datatype.ID;

public class Resource {

    // attributes
    private ID identifier; // M
    private String type; // M
    private String scormType; // M
    private String href; // O
    private AnyURI xmlBase; // O

    // elements
    private Metadata metadata; // 0...1
    private List<File> fileList; // 0...1
    private List<Dependency> dependencyList; // 0...n

}
