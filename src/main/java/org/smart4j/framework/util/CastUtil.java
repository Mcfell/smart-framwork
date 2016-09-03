package org.smart4j.framework.util;

/**
 * Created by Mcfell on 2016/8/29.
 */
public class CastUtil {
    public static int castInt(Object property) {
        return castInt(property,0);
    }

    private static int castInt(Object property, int defaultValue) {
        int intValue = defaultValue;
        if (property != null ) {
            String strValue = castString(property);
            if (StringUtil.isNotEmpty(strValue)) {
                try {
                    intValue = Integer.parseInt(strValue);
                } catch (NumberFormatException e) {
                    intValue = defaultValue;
                }
            }
        }
        return intValue;
    }

    public static String castString(Object property) {
        return castString(property,"");
    }

    private static String castString(Object property, String defaultValue) {
        return property == null? defaultValue : String.valueOf(property);
    }

    public static boolean castBoolean(Object property) {
        return castBoolean(property,false);
    }

    private static boolean castBoolean(Object property, boolean defaultValue) {
        boolean value = defaultValue;
        if (property != null) {
            value = Boolean.parseBoolean(castString(property));
        }
        return value;
    }
}
