package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import com.corkili.learningserver.scorm.cam.model.datatype.IDRef;

/**
 * Organizations: Contains the content structure or organization
 * of the learning resources making up a stand-alone unit or
 * units of instruction. A definition of sequencing intent can
 * be associated with the content structure.
 */
public class Organizations {

    // attributes
    private IDRef defaultOrganizationID; // M

    // elements
    private List<Organization> organizationList; // 0...n

    public Organizations() {
        organizationList = new ArrayList<>();
    }

    public IDRef getDefaultOrganizationID() {
        return defaultOrganizationID;
    }

    public void setDefaultOrganizationID(IDRef defaultOrganizationID) {
        this.defaultOrganizationID = defaultOrganizationID;
    }

    public List<Organization> getOrganizationList() {
        return organizationList;
    }

    public void setOrganizationList(List<Organization> organizationList) {
        this.organizationList = organizationList;
    }
}
