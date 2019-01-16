package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

import com.corkili.learningserver.scorm.cam.model.datatype.AnyURI;

/**
 * Defines the learning resources bundled in the content package.
 */
public class Resources {

    // attributes
    private AnyURI xmlBase;

    // elements
    private List<Resource> resourceList;

}
