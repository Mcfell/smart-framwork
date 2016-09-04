package org.smart4j.framework.util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * Created by A8401 on 16/9/4.
 */
public final class ArrayUtil {

    public static final boolean isEmpty(Object[] array) {
        return ArrayUtils.isEmpty(array);
    }
    public static final boolean isNotEmpty(Object[] array) {
        return !isEmpty(array);
    }
}
