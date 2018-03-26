package com.enlightent.util;

import org.apache.commons.lang3.StringUtils;

public abstract class Assert {

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notBlank(CharSequence cs, String message) {
        if (StringUtils.isBlank(cs)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notFalse(boolean bool, String message) {
        if (bool == false) {
            throw new IllegalArgumentException(message);
        }
    }
}
