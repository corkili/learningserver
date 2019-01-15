package com.corkili.learningserver.scorm.cam.xml;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import com.corkili.learningserver.scorm.common.Limit;

/**
 * Describe a xml element.
 */
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public final class Element {

    private final String elementName;
    private final String xmlNamespace;
    private final String defaultXmlNamespacePrefix;
    private final Multiplicity multiplicityForContentAggregation;
    private final Multiplicity multiplicityForResource;
    private final boolean isContainer;
    private final XmlDataType xmlDataType;
    private final String defaultValue;
    private final boolean hasLimitOfValue;
    private final Limit<String> limitOfValue;
    private final int spm;
    private final List<Attribute> attributes;
    private final List<Element> elements;

}
