package com.corkili.learningserver.scorm.rte.api;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.scorm.common.CommonUtils;
import com.corkili.learningserver.scorm.rte.model.RuntimeDataModel;
import com.corkili.learningserver.scorm.rte.model.annotation.Meta;
import com.corkili.learningserver.scorm.rte.model.datatype.CollectionDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.error.Diagnostic;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.CollectionScormResult;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

@Slf4j
public class ElementParser {

    private List<CollectionDataType> accessedCollectionElement = new LinkedList<>();

    public ScormResult parseSet(RuntimeDataModel runtimeDataModel, String elementName, String value) {
        return parse(runtimeDataModel, true, elementName, value);
    }

    public ScormResult parseGet(RuntimeDataModel runtimeDataModel, String elementName) {
        return parse(runtimeDataModel, false, elementName, null);
    }

    private ScormResult parse(RuntimeDataModel runtimeDataModel, boolean isSet, String elementName, String value) {
        if (StringUtils.isBlank(elementName)) {
            return new ScormResult(isSet ? "false" : "", isSet ? ScormError.E_351 : ScormError.E_301,
                    Diagnostic.DATA_MODEL_ELEMENT_NOT_SPECIFIED);
        }
        String[] elementNames = elementName.split("\\.");
        ScormResult scormResult = parse(runtimeDataModel, elementNames, 0, isSet, value);
        boolean success = scormResult.getError().equals(ScormError.E_0);
        for (CollectionDataType collectionElement : accessedCollectionElement) {
            collectionElement.syncNewInstance(success);
        }
        accessedCollectionElement.clear();
        return scormResult;
    }


    private ScormResult parse(Object element, String[] elementNames, int index, boolean isSet, String value) {
        if (index >= elementNames.length) {
            return generateScormResult(elementNames, index, isSet);
        }
        if (isInteger(elementNames[index])) {
            CollectionDataType collectionElement = (CollectionDataType) element;
            CollectionScormResult collectionScormResult;
            if (isSet) {
                collectionScormResult = collectionElement.set(Integer.parseInt(elementNames[index]));
            } else {
                collectionScormResult = collectionElement.get(Integer.parseInt(elementNames[index]));
            }
            if (!collectionScormResult.getError().equals(ScormError.E_0)) {
                return collectionScormResult;
            } else {
                accessedCollectionElement.add(collectionElement);
                Object instance = collectionScormResult.getInstance();
                if (instance instanceof TerminalDataType){
                    return parse((TerminalDataType) instance, isSet, value);
                } else {
                    return parse(instance, elementNames, index + 1, isSet, value);
                }
            }
        } else {
            for (Field field : element.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Meta.class) && field.getDeclaredAnnotation(Meta.class).value().equals(elementNames[index])) {
                    try {
                        field.setAccessible(true);
                        Object childElement = field.get(element);
                        if (childElement instanceof TerminalDataType){
                            return parse((TerminalDataType) childElement, isSet, value);
                        } else {
                            return parse(childElement, elementNames, index + 1, isSet, value);
                        }
                    } catch (IllegalAccessException e) {
                        log.error(CommonUtils.stringifyError(e));
                    } finally {
                        field.setAccessible(false);
                    }
                    break;
                }
            }
            return generateScormResult(elementNames, index, isSet);
        }
    }

    private ScormResult parse(TerminalDataType terminalElement, boolean isSet, String value) {
        ScormResult scormResult;
        if (isSet) {
            scormResult = terminalElement.set(value);
        } else {
            scormResult = terminalElement.get();
        }
        return scormResult;
    }

    private ScormResult generateScormResult(String[] elementNames, int index, boolean isSet) {
        if (index == elementNames.length - 1) {
            switch (elementNames[index]) {
                case "_children":
                    return new ScormResult(isSet ? "false" : "", isSet ? ScormError.E_351 : ScormError.E_301,
                            Diagnostic.DATA_MODEL_ELEMENT_DOSE_NOT_HAVE_CHILDREN);
                case "_count":
                    return new ScormResult(isSet ? "false" : "", isSet ? ScormError.E_351 : ScormError.E_301,
                            Diagnostic.DATA_MODEL_ELEMENT_CANNOT_HAVE_COUNT);
                case "_version":
                    return new ScormResult(isSet ? "false" : "", isSet ? ScormError.E_351 : ScormError.E_301,
                            Diagnostic.DATA_MODEL_ELEMENT_DOSE_NOT_HAVE_VERSION);
            }
        }
        return new ScormResult(isSet ? "false" : "", ScormError.E_401);
    }

    private boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
