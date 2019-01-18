package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("identifier", identifier)
                .append("version", version)
                .append("xmlBase", xmlBase)
                .append("metadata", metadata)
                .append("organizations", organizations)
                .append("resources", resources)
                .append("sequencingCollection", sequencingCollection)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Manifest manifest = (Manifest) o;

        return new EqualsBuilder()
                .append(identifier, manifest.identifier)
                .append(version, manifest.version)
                .append(xmlBase, manifest.xmlBase)
                .append(metadata, manifest.metadata)
                .append(organizations, manifest.organizations)
                .append(resources, manifest.resources)
                .append(sequencingCollection, manifest.sequencingCollection)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(identifier)
                .append(version)
                .append(xmlBase)
                .append(metadata)
                .append(organizations)
                .append(resources)
                .append(sequencingCollection)
                .toHashCode();
    }
}
