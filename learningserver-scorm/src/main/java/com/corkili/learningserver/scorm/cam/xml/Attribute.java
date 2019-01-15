package com.corkili.learningserver.scorm.cam.xml;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import com.corkili.learningserver.scorm.common.Limit;

/**
 * Describe the attribute of element.
 */
@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public final class Attribute {

    private final String attributeName;
    private final boolean isMandatory;
    private final XmlDataType xmlDataType;
    private final String defaultValue;
    private final boolean hasLimitOfValue;
    private final Limit<String> limitOfValue;
    private final int spm;

}
