package com.corkili.learningserver.scorm.cam.model;

import com.corkili.learningserver.scorm.cam.model.datatype.AnyURI;
import com.corkili.learningserver.scorm.cam.model.datatype.ID;

/**
 * A manifest is an XML document that contains a
 * structured inventory of the content of a package.
 * In this version, ADL recommends not to use (sub)manifests.
 */

public class Manifest {

    // attributes
    private ID identifier;  // M
    private String version; // O
    private AnyURI xmlBase; // O

    // elements
    private ManifestMetadata metadata; // 1...1
    private Organizations organizations; // 1...1
    private Resources resources; // 1...1
    private SequencingCollection sequencingCollection; // 0...1
//  private List<Manifest> subManifests; // don't implementation

    public Manifest() {
    }

    public ID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(ID identifier) {
        this.identifier = identifier;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public AnyURI getXmlBase() {
        return xmlBase;
    }

    public void setXmlBase(AnyURI xmlBase) {
        this.xmlBase = xmlBase;
    }

    public ManifestMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ManifestMetadata metadata) {
        this.metadata = metadata;
    }

    public Organizations getOrganizations() {
        return organizations;
    }

    public void setOrganizations(Organizations organizations) {
        this.organizations = organizations;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public SequencingCollection getSequencingCollection() {
        return sequencingCollection;
    }

    public void setSequencingCollection(SequencingCollection sequencingCollection) {
        this.sequencingCollection = sequencingCollection;
    }
}
