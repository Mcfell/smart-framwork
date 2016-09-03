package org.smart4j.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mcfell on 2016/8/29.
 */
public class StringUtil {
    public static boolean isEmpty(String value) {
        if (value == null){
            return true;
        }
        return StringUtils.isEmpty(value);
    }
    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }
}
