package com.corkili.learningserver.scorm.cam.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("kind", kind)
                .append("resource", resource)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Relation relation = (Relation) o;

        return new EqualsBuilder()
                .append(kind, relation.kind)
                .append(resource, relation.resource)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(kind)
                .append(resource)
                .toHashCode();
    }
}
