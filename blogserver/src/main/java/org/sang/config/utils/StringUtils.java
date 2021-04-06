package org.sang.config.utils;

public class StringUtils {
    public static boolean isEmpty(String source) {
        return source == null || source.length() == 0 || source.equals("null") || source.equals(" ");
    }
}
