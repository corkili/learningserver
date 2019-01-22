package com.corkili.learningserver.scorm.cam.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.corkili.learningserver.scorm.cam.model.datatype.ID;

public class Item {

    // attributes
    private ID identifier; // M
    private String identifierref; // O
    private boolean isvisible; // O true
    private String parameters; // O 格式限制

    // elements
    private String title; // 1...1
    private List<Item> itemList; // 0...n
    private Metadata metadata; // 0...1
    private String timeLimitAction; // 0...1 {exit,message}{exit,no message}{continue,message}{continue,no message}
    private String dataFromLMS; // 0...1
    private CompletionThreshold completionThreshold; // 0...1
    private Data data; // 0...1
    private Sequencing sequencing; // 0...1
    private Presentation presentation; // 0...1

    public Item() {
        isvisible = true;
        itemList = new ArrayList<>();
    }

    public ID getIdentifier() {
        return identifier;
    }

    public void setIdentifier(ID identifier) {
        this.identifier = identifier;
    }

    public String getIdentifierref() {
        return identifierref;
    }

    public void setIdentifierref(String identifierref) {
        this.identifierref = identifierref;
    }

    public boolean isIsvisible() {
        return isvisible;
    }

    public void setIsvisible(boolean isvisible) {
        this.isvisible = isvisible;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
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

    public String getTimeLimitAction() {
        return timeLimitAction;
    }

    public void setTimeLimitAction(String timeLimitAction) {
        this.timeLimitAction = timeLimitAction;
    }

    public String getDataFromLMS() {
        return dataFromLMS;
    }

    public void setDataFromLMS(String dataFromLMS) {
        this.dataFromLMS = dataFromLMS;
    }

    public CompletionThreshold getCompletionThreshold() {
        return completionThreshold;
    }

    public void setCompletionThreshold(CompletionThreshold completionThreshold) {
        this.completionThreshold = completionThreshold;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Sequencing getSequencing() {
        return sequencing;
    }

    public void setSequencing(Sequencing sequencing) {
        this.sequencing = sequencing;
    }

    public Presentation getPresentation() {
        return presentation;
    }

    public void setPresentation(Presentation presentation) {
        this.presentation = presentation;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("identifier", identifier)
                .append("identifierref", identifierref)
                .append("isvisible", isvisible)
                .append("parameters", parameters)
                .append("title", title)
                .append("itemList", itemList)
                .append("metadata", metadata)
                .append("timeLimitAction", timeLimitAction)
                .append("dataFromLMS", dataFromLMS)
                .append("completionThreshold", completionThreshold)
                .append("data", data)
                .append("sequencing", sequencing)
                .append("presentation", presentation)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        return new EqualsBuilder()
                .append(isvisible, item.isvisible)
                .append(identifier, item.identifier)
                .append(identifierref, item.identifierref)
                .append(parameters, item.parameters)
                .append(title, item.title)
                .append(itemList, item.itemList)
                .append(metadata, item.metadata)
                .append(timeLimitAction, item.timeLimitAction)
                .append(dataFromLMS, item.dataFromLMS)
                .append(completionThreshold, item.completionThreshold)
                .append(data, item.data)
                .append(sequencing, item.sequencing)
                .append(presentation, item.presentation)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(identifier)
                .append(identifierref)
                .append(isvisible)
                .append(parameters)
                .append(title)
                .append(itemList)
                .append(metadata)
                .append(timeLimitAction)
                .append(dataFromLMS)
                .append(completionThreshold)
                .append(data)
                .append(sequencing)
                .append(presentation)
                .toHashCode();
    }
}
