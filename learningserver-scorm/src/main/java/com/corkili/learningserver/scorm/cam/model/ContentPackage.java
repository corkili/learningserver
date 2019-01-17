package com.corkili.learningserver.scorm.cam.model;

import com.corkili.learningserver.scorm.cam.MultiplicityAssertor;
import com.corkili.learningserver.scorm.common.NameAndValue;

/**
 * A package represents a unit of learning.
 */
public class ContentPackage {

    private Manifest manifest; // 1
    private Content content;

    public ContentPackage(Manifest manifest) {
        setManifest(manifest);
        this.content = new Content(); // TODO: 根据manifest自动解析
    }

    public Manifest getManifest() {
        return manifest;
    }

    public void setManifest(Manifest manifest) {
        checkMultiplicityOfManifest(manifest);
        this.manifest = manifest;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    private void checkMultiplicityOfManifest(Manifest manifest) {
        //noinspection unchecked
        MultiplicityAssertor.assertOneAndOnlyOne("manifest.xml",
                new NameAndValue<>("manifest", manifest));
    }
}
