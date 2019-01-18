package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.corkili.learningserver.scorm.cam.model.datatype.ID;

public class Organization {

    // attributes
    private ID identifier; // M
    private String structure; // O hierarchical
    private boolean objectivesGlobalToSystem; // O true
    private boolean sharedDataGlobalToSystem; // O true

    // elements
    private String title; // 1...1
    private List<Item> itemList; // 1...n
    private Metadata metadata; // 0..1
    private CompletionThreshold completionThreshold; // 0...1
    private Sequencing sequencing; // 0...1

    public Organization() {
        structure = "hierarchical";
        objectivesGlobalToSystem = true;
        sharedDataGlobalToSystem = true;
        itemList = new ArrayList<>();
    }

    public ID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(ID identifier) {
        this.identifier = identifier;
    }

    public String getStructure() {
        return structure;
    }

    public void setStructure(String structure) {
        this.structure = structure;
    }

    public boolean isObjectivesGlobalToSystem() {
        return objectivesGlobalToSystem;
    }

    public void setObjectivesGlobalToSystem(boolean objectivesGlobalToSystem) {
        this.objectivesGlobalToSystem = objectivesGlobalToSystem;
    }

    public boolean isSharedDataGlobalToSystem() {
        return sharedDataGlobalToSystem;
    }

    public void setSharedDataGlobalToSystem(boolean sharedDataGlobalToSystem) {
        this.sharedDataGlobalToSystem = sharedDataGlobalToSystem;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public CompletionThreshold getCompletionThreshold() {
        return completionThreshold;
    }

    public void setCompletionThreshold(CompletionThreshold completionThreshold) {
        this.completionThreshold = completionThreshold;
    }

    public Sequencing getSequencing() {
        return sequencing;
    }

    public void setSequencing(Sequencing sequencing) {
        this.sequencing = sequencing;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("identifier", identifier)
                .append("structure", structure)
                .append("objectivesGlobalToSystem", objectivesGlobalToSystem)
                .append("sharedDataGlobalToSystem", sharedDataGlobalToSystem)
                .append("title", title)
                .append("itemList", itemList)
                .append("metadata", metadata)
                .append("completionThreshold", completionThreshold)
                .append("sequencing", sequencing)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Organization that = (Organization) o;

        return new EqualsBuilder()
                .append(objectivesGlobalToSystem, that.objectivesGlobalToSystem)
                .append(sharedDataGlobalToSystem, that.sharedDataGlobalToSystem)
                .append(identifier, that.identifier)
                .append(structure, that.structure)
                .append(title, that.title)
                .append(itemList, that.itemList)
                .append(metadata, that.metadata)
                .append(completionThreshold, that.completionThreshold)
                .append(sequencing, that.sequencing)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(identifier)
                .append(structure)
                .append(objectivesGlobalToSystem)
                .append(sharedDataGlobalToSystem)
                .append(title)
                .append(itemList)
                .append(metadata)
                .append(completionThreshold)
                .append(sequencing)
                .toHashCode();
    }
}
