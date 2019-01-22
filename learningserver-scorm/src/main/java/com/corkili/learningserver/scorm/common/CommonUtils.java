package com.corkili.learningserver.scorm.common;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CommonUtils {

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
