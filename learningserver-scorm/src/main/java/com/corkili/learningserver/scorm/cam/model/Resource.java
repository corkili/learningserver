package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

import com.corkili.learningserver.scorm.cam.model.datatype.AnyURI;
import com.corkili.learningserver.scorm.cam.model.datatype.File;
import com.corkili.learningserver.scorm.cam.model.datatype.ID;

public class Resource {

    // attributes
    private ID identifier;
    private String type;
    private String href;
    private AnyURI xmlBase;
    private String scormType;

    // elements
    private Metadata metadata;
    private List<File> fileList;
    private List<Dependency> dependencyList;

}
