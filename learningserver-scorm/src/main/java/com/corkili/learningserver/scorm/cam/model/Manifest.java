package com.corkili.learningserver.scorm.cam.model;

import java.util.List;

/**
 * A manifest is an XML document that contains a
 * structured inventory of the content of a package.
 * In this version, ADL recommends not to use (sub)manifests.
 */
public class Manifest {

    private ManifestMetadata metadata;
    private Organizations organizations;
    private Resources resources;
    private List<Manifest> subManifests; // don't implementation

}
