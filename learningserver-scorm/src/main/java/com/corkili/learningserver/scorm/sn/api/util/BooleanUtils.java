package com.corkili.learningserver.scorm.sn.api.util;

public class BooleanUtils {

    public static Boolean negate(Boolean raw) {
        if (raw == null) {
            return null;
        } else {
            return !raw;
        }
    }

    public static Boolean and(Boolean... values) {
        if (values.length == 0) {
            return null;
        }
        if (values.length == 1) {
            return values[0];
        }
        Boolean res = values[0];
        for (int i = 1; i < values.length; i++) {
            res = and(res, values[i]);
        }
        return res;
    }

    public static Boolean or(Boolean... values) {
        if (values.length == 0) {
            return null;
        }
        if (values.length == 1) {
            return values[0];
        }
        Boolean res = values[0];
        for (int i = 1; i < values.length; i++) {
            res = or(res, values[i]);
        }
        return res;
    }

    private static Boolean and(Boolean one, Boolean two) {
        if (one == null && two == null) {
            return null;
        }
        if (one == null || two == null) {
            if (one == null) {
                return two ? null : false;
            } else {
                return one ? null : false;
            }
        }
        return one && two;
    }

    private static Boolean or(Boolean one, Boolean two) {
        if (one == null && two == null) {
            return null;
        }
        if (one == null || two == null) {
            if (one == null) {
                return two ? true : null;
            } else {
                return one ? true : null;
            }
        }
        return one || two;
    }
}
