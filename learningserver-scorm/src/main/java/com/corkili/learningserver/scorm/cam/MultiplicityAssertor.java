package com.corkili.learningserver.scorm.cam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.corkili.learningserver.scorm.cam.exception.MultiplicityNotConformedException;
import com.corkili.learningserver.scorm.common.NameAndValue;

public final class MultiplicityAssertor {

    public static void assertOneAndOnlyOne(String parentElementName, NameAndValue<Object>... elements) {
        List<String> invalidElementNames = new ArrayList<>(elements.length / 2);
        for (NameAndValue<Object> element : elements) {
            if (element.getValue() == null) {
                invalidElementNames.add(element.getName());
            }
        }
        if (!invalidElementNames.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            msg.append("The following attribute(s) or element(s) within ");
            msg.append(parentElementName);
            msg.append(" must exist 1 and only 1 time: [");
            invalidElementNames.forEach(name -> msg.append(name).append(","));
            msg.replace(msg.length() - 1, msg.length(), "].");
            throw new MultiplicityNotConformedException(msg.toString());
        }
    }

    public static void assertZeroOrMore(String parentElementName, NameAndValue<Collection<?>>... elements) {
        List<String> invalidElementNames = new ArrayList<>(elements.length / 2);
        for (NameAndValue<Collection<?>> element : elements) {
            if (element.getValue() == null) {
                invalidElementNames.add(element.getName());
            }
        }
        if (!invalidElementNames.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            msg.append("The following attribute(s) or element(s) within ");
            msg.append(parentElementName);
            msg.append(" shall exist 0 or More times: [");
            invalidElementNames.forEach(name -> msg.append(name).append(","));
            msg.replace(msg.length() - 1, msg.length(), "].");
            throw new MultiplicityNotConformedException(msg.toString());
        }
    }

    public static void assertOneOrMore(String parentElementName, NameAndValue<Collection<?>>... elements) {
        List<String> invalidElementNames = new ArrayList<>(elements.length / 2);
        for (NameAndValue<Collection<?>> element : elements) {
            if (element.getValue() == null || element.getValue().isEmpty()) {
                invalidElementNames.add(element.getName());
            }
        }
        if (!invalidElementNames.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            msg.append("The following attribute(s) or element(s) within ");
            msg.append(parentElementName);
            msg.append(" shall exist 1 or More times: [");
            invalidElementNames.forEach(name -> msg.append(name).append(","));
            msg.replace(msg.length() - 1, msg.length(), "].");
            throw new MultiplicityNotConformedException(msg.toString());
        }
    }


}
