package com.corkili.learningserver.scorm.rte.model.util;

import com.corkili.learningserver.scorm.rte.model.RuntimeData;
import com.corkili.learningserver.scorm.rte.model.annotation.Meta;
import com.corkili.learningserver.scorm.rte.model.datatype.CollectionDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.GeneralDataType;
import com.corkili.learningserver.scorm.rte.model.datatype.TerminalDataType;
import com.corkili.learningserver.scorm.rte.model.error.ScormError;
import com.corkili.learningserver.scorm.rte.model.result.CollectionScormResult;
import com.corkili.learningserver.scorm.rte.model.result.ScormResult;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuntimeDataUtil {

    public static String transferToString(RuntimeData runtimeData) {
        Map<String, String> map = new HashMap<>();
        transferToMap(runtimeData, "", map);
        List<String> nvs = new ArrayList<>();
        map.forEach((name, value) -> nvs.add(name + ">>:" + value));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < nvs.size(); i++) {
            sb.append(nvs.get(i));
            if (i != nvs.size() - 1) {
                sb.append("<:>");
            }
        }
        return sb.toString();
    }

    public static Map<String, String> stirng2map(String runtimeDataStr) {
        String[] nsa = runtimeDataStr.split("<:>");
        Arrays.sort(nsa);
        Map<String, String> map = new HashMap<>();
        for (String ns : nsa) {
            String[] nameAndValue = ns.split(">>:");
            if (nameAndValue.length != 2) {
                continue;
            }
            String name = nameAndValue[0];
            String value = nameAndValue[1];
            map.put(name, value);
        }
        return map;
    }

    private static void transferToMap(Object element, String name, Map<String, String> map) {
        if (element instanceof GeneralDataType) {
            for (Field field : element.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Meta.class)) {
                    Meta meta = field.getDeclaredAnnotation(Meta.class);
                    if (meta.readable() && meta.writable()) {
                        try {
                            field.setAccessible(true);
                            Object childElement = field.get(element);
                            if (name.isEmpty()) {
                                transferToMap(childElement, meta.value(), map);
                            } else {
                                transferToMap(childElement, name  + "." + meta.value(), map);
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } finally {
                            field.setAccessible(false);
                        }
                    }
                }
            }
        } else if (element instanceof TerminalDataType) {
            TerminalDataType terminalData = (TerminalDataType) element;
            ScormResult scormResult = terminalData.get();
            if (scormResult.getError() == ScormError.E_0) {
                map.put(name, scormResult.getReturnValue());
            }
        } else if (element instanceof CollectionDataType) {
            CollectionDataType collectionData = (CollectionDataType) element;
            int cnt = collectionData.count();
            List<Object> instances = new ArrayList<>();
            boolean hasError = false;
            for (int index = 0; index < cnt; index++) {
                CollectionScormResult collectionScormResult = collectionData.get(index);
                if (collectionScormResult.getError() != ScormError.E_0) {
                    hasError = true;
                    break;
                } else {
                    instances.add(collectionScormResult.getInstance());
                }
            }
            if (!hasError && instances.size() == cnt) {
                for (int index = 0; index < instances.size(); index++) {
                    Object instance = instances.get(index);
                    transferToMap(instance, name + "." + index, map);
                }
            }
        }
    }
}
