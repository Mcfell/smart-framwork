package org.smart4j.framework.helper;

import org.smart4j.framework.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Mcfell on 16/9/3.
 */
public class BeanHelper {
    /**
     * 定义Bean映射(用语存放Service和Controll类)
     */
    private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();

    static {
        Set<Class<?>> beanSet = ClassHelper.getBeanSet();
        for (Class<?> cls : beanSet) {
            Object object = ReflectionUtil.newInstance(cls);
            BEAN_MAP.put(cls,object);
        }
    }

    public static Map<Class<?>, Object> getBeanMap() {
        return BEAN_MAP;
    }

    public static <T> T getBean(Class<T> cls) {
        if (!BEAN_MAP.containsKey(cls)) {
            throw new RuntimeException("Can not get bean by class:"+ cls);
        }
        return (T)BEAN_MAP.get(cls);
    }
}
