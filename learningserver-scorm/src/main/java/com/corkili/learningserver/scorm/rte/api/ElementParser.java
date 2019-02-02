package com.corkili.learningserver.scorm.rte.api;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

import com.corkili.learningserver.scorm.common.CommonUtils;
import com.corkili.learningserver.scorm.rte.model.RuntimeData;
import com.corkili.learningserver.scorm.rte.model.annotation.Meta;
import com.corkili.learningserver.scorm.rte.model.datatype.CollectionDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.MapDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.error.Diagnostic;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.CollectionScormResult;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

@Slf4j
public class ElementParser implements Delayed {

    private static DelayQueue<ElementParser> parserPool;

    static {
        parserPool = new DelayQueue<>();
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> parserPool.drainTo(new LinkedList<>()),
                        1, 1, TimeUnit.SECONDS);
    }

    private List<CollectionDataType> accessedCollectionElement;
    private long lastAccessTime;

    private ElementParser() {
        accessedCollectionElement = new LinkedList<>();
        lastAccessTime = System.currentTimeMillis();
    }

    static ScormResult parseSet(RuntimeData runtimeData, String elementName, String value) {
        return getInstance().parse(runtimeData, true, elementName, value);
    }

    static ScormResult parseGet(RuntimeData runtimeData, String elementName) {
        return getInstance().parse(runtimeData, false, elementName, null);
    }

    private static ElementParser getInstance() {
        ElementParser parser = parserPool.poll();
        if (parser == null) {
            parser = new ElementParser();
        }
        return parser;
    }

    private static void release(ElementParser parser) {
        parser.lastAccessTime = System.currentTimeMillis();
        parserPool.offer(parser);
    }

    private ScormResult parse(RuntimeData runtimeData, boolean isSet, String elementName, String value) {
        if (StringUtils.isBlank(elementName)) {
            return new ScormResult(isSet ? "false" : "", isSet ? ScormError.E_351 : ScormError.E_301,
                    Diagnostic.DATA_MODEL_ELEMENT_NOT_SPECIFIED);
        }
        String[] elementNames = elementName.split("\\.");
        ScormResult scormResult = parse(runtimeData, elementNames, 0, isSet, value);
        boolean success = scormResult.getError().equals(ScormError.E_0);
        for (CollectionDataType collectionElement : accessedCollectionElement) {
            collectionElement.syncNewInstance(success);
        }
        accessedCollectionElement.clear();
        release(this);
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
                } else if (instance instanceof MapDataType) {
                    String key = index == elementNames.length - 1 ? null : elementNames[index + 1];
                    return parse((MapDataType) instance, isSet, key, value);
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
                        } else if (childElement instanceof MapDataType) {
                            String key = index == elementNames.length - 1 ? null : elementNames[index + 1];
                            return parse((MapDataType) childElement, isSet, key, value);
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

    private ScormResult parse(MapDataType mapElement, boolean isSet, String key, String value) {
        ScormResult scormResult;
        if (isSet) {
            scormResult = mapElement.set(key, value);
        } else {
            scormResult = mapElement.get(key);
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

    @Override
    public long getDelay(TimeUnit unit) {
        return System.currentTimeMillis() - (lastAccessTime + 600000);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int)(this.lastAccessTime - ((ElementParser) o).lastAccessTime);
    }
}
