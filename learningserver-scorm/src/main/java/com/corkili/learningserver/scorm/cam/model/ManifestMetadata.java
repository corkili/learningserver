package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Metadata: Data describing the content package as a whole.
 */
public class ManifestMetadata {

    private String schema; // 1...1 {ADL SCORM}
    private String schemaVersion; // 1...1 {2004 4th Edition}
    private Metadata metadata; // 0...n

    public ManifestMetadata() {
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(String schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("schema", schema)
                .append("schemaVersion", schemaVersion)
                .append("metadata", metadata)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ManifestMetadata that = (ManifestMetadata) o;

        return new EqualsBuilder()
                .append(schema, that.schema)
                .append(schemaVersion, that.schemaVersion)
                .append(metadata, that.metadata)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(schema)
                .append(schemaVersion)
                .append(metadata)
                .toHashCode();
    }
}
