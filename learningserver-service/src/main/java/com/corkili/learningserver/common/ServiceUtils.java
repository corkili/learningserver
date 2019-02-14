package com.corkili.learningserver.common;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.corkili.learningserver.po.PersistObject;

public class ServiceUtils {

    public static List<String> string2List(String content, Pattern delimiterRegex) {
        if (StringUtils.isBlank(content) || delimiterRegex == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(content.split(delimiterRegex.pattern())));
    }
    
    public static String list2String(List<?> list, String delimiter) {
        if (list.isEmpty() || StringUtils.isBlank(delimiter)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        int lastIndex = list.size() - 1;
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i));
            if (i != lastIndex) {
                sb.append(delimiter);
            }
        }
        return sb.toString();
    }

    public static boolean isEntityReferenceNull(PersistObject po) {
        return po == null || po.getId() == null;
    }

    public static String stringifyError(Throwable error) {
        StringWriter result = new StringWriter();
        PrintWriter printer = new PrintWriter(result);
        error.printStackTrace(printer);
        printer.close();
        return result.toString();
    }

    public static String format(String format, Object... objects) {
        return String.format(format.replace("{}", "%s"), objects);
    }

}
