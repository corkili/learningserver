package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

/**
 * Metadata: Data describing the content package as a whole.
 */
public class ManifestMetadata {

    private String schema; // 1...1
    private String schemaVersion; // 1...1
    private List<Metadata> metadataList; // 0...n

}
