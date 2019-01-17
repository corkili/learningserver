package com.corkili.learningserver.scorm.cam;

import java.util.ArrayList;
import java.util.List;

import com.corkili.learningserver.scorm.cam.exception.RequiredAttributeNotExistException;
import com.corkili.learningserver.scorm.common.NameAndValue;

public class AttributeAssertor {

    public static void assertAttributeIsMandatory(String parentElementName, NameAndValue<String>... attributes) {
        List<String> invalidAttributeNames = new ArrayList<>(attributes.length / 2);
        for (NameAndValue<String> attribute : attributes) {
            if (attribute.getValue() == null || attribute.getValue().trim().isEmpty()) {
                invalidAttributeNames.add(attribute.getName());
            }
        }
        if (!invalidAttributeNames.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            msg.append("The following attribute(s) or element(s) within ");
            msg.append(parentElementName);
            msg.append(" must exist: [");
            invalidAttributeNames.forEach(name -> msg.append(name).append(","));
            msg.replace(msg.length() - 1, msg.length(), "].");
            throw new RequiredAttributeNotExistException(msg.toString());
        }
    }

}
