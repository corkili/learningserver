package com.corkili.learningserver.scorm.cam;

import java.util.Collection;

import com.corkili.learningserver.scorm.cam.exception.MultiplicityNotConformedException;
import com.corkili.learningserver.scorm.common.CommonUtils;

public final class MultiplicityAssertor {

    public static void assertOneAndOnlyOne(String parentElementName, String elementName, Object element) {
        if (element == null) {
            throw new MultiplicityNotConformedException(CommonUtils.format(
                    "The element \"{}\" in the \"{}\" must exist 1 and only 1 time.", elementName, parentElementName));
        }
    }

    public static void assertZeroOrMore(String parentElementName, String elementName, Collection<?> element) {
        if (element == null) {
            throw new MultiplicityNotConformedException(CommonUtils.format(
                    "The element \"{}\" in the \"{}\" shall exist 0 or more time. If exist 0 time, please " +
                            "pass a \"empty list\" as parameter.", elementName, parentElementName));
        }
    }

    public static void assertOneOrMore(String parentElementName, String elementName, Collection<?> element) {
        if (element == null || element.isEmpty()) {
            throw new MultiplicityNotConformedException(CommonUtils.format(
                    "The element \"{}\" in the \"{}\" shall exist 1 or more time.", elementName, parentElementName));
        }
    }


}
