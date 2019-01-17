package com.corkili.learningserver.scorm.cam.model;

public class Relation {

    private Vocabulary kind; // 0...1
    private RelationResource resource; // 0...1

    public Relation() {
    }

    public Vocabulary getKind() {
        return kind;
    }

    public void setKind(Vocabulary kind) {
        this.kind = kind;
    }

    public RelationResource getResource() {
        return resource;
    }

    public void setResource(RelationResource resource) {
        this.resource = resource;
    }
}
