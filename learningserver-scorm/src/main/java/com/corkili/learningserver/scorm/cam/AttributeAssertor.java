package com.corkili.learningserver.scorm.cam;

import com.corkili.learningserver.scorm.cam.exception.EmptyValueOfAttributeException;
import com.corkili.learningserver.scorm.cam.exception.RequiredAttributeNotExistException;
import com.corkili.learningserver.scorm.common.CommonUtils;

public class AttributeAssertor {

    public static void assertAttributeIsMandatory(String parentElementName, String attributeName, Object attribute) {
        if (attribute == null || attribute.toString().trim().isEmpty()) {
            throw new RequiredAttributeNotExistException(CommonUtils.format(
                    "The attribute \"{}\" in the \"{}\" shall exist 1 or more time.", attributeName, parentElementName));
        }
    }

    public static void assertAttributeNotEmpty(String parentElementName, String attributeName, Object attribute) {
        if (attribute != null && attribute.toString().trim().isEmpty()) {
            throw new EmptyValueOfAttributeException(CommonUtils.format(
                    "The value of attribute \"{}\" in the \"{}\" cannot be empty.", attributeName, parentElementName));
        }
    }

}
