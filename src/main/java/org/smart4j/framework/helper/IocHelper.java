package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.util.ArrayUtil;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by Mcfell on 16/9/4.
 */
public final class IocHelper {
    static {
        Map<Class<?>, Object> beanMap = BeanHelper.getBeanMap();
        if (CollectionUtil.isNotEmpty(beanMap)) {
            for (Map.Entry<Class<?>,Object> mapEntry : beanMap.entrySet()) {
                Class<?> cls = mapEntry.getKey();
                Object obj = mapEntry.getValue();
                Field[] declaredFields = cls.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(declaredFields)) {
                    for (Field filed : declaredFields) {
                        if (!filed.isAnnotationPresent(Inject.class)) {
                                continue;
                            }
                            Class<?> filedType = filed.getType();
                            Object fieldInstance = beanMap.get(filedType);
                            if (fieldInstance != null) {
                                ReflectionUtil.setField(obj,filed,beanMap.get(filed.getType()));
                            }
                        }
                }

            }
        }
    }
}
