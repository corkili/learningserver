package com.corkili.learningserver.scorm.cam.xml;

/**
 * Describe the multiplicity requirement of element.
 */
public enum Multiplicity {
    /**
     * The element must exist 1 and only 1 time within the parent element.
     */
    ONE_AND_ONLY_ONE,
    /**
     * The element can exist 0 or More times within the parent element.
     */
    ZERO_OR_MORE,
    /**
     * The element must exist 1 or More times within the parent element.
     */
    ONE_OR_MORE,
    /**
     * The element is not permitted.
     */
    ZERO,
    /**
     * The element can exist 0 or 1 time within the parent element.
     */
    ZERO_OR_ONE;
}
