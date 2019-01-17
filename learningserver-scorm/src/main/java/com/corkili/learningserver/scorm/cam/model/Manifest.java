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

}
