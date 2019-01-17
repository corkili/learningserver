package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Metadata: Data describing the content package as a whole.
 */
public class ManifestMetadata {

    private String schema; // 1...1 {ADL SCORM}
    private String schemaVersion; // 1...1 {2004 4th Edition}
    private List<Metadata> metadataList; // 0...n

    public ManifestMetadata() {
        metadataList = new ArrayList<>();
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

    public List<Metadata> getMetadataList() {
        return metadataList;
    }

    public void setMetadataList(List<Metadata> metadataList) {
        this.metadataList = metadataList;
    }
}
