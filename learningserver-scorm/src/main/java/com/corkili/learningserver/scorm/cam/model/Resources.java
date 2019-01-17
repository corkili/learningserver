package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import com.corkili.learningserver.scorm.cam.model.datatype.AnyURI;

/**
 * Defines the learning resources bundled in the content package.
 */
public class Resources {

    // attributes
    private AnyURI xmlBase; // O

    // elements
    private List<Resource> resourceList; // 0...n

    public Resources() {
        resourceList = new ArrayList<>();
    }

    public AnyURI getXmlBase() {
        return xmlBase;
    }

    public void setXmlBase(AnyURI xmlBase) {
        this.xmlBase = xmlBase;
    }

    public List<Resource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<Resource> resourceList) {
        this.resourceList = resourceList;
    }
}
