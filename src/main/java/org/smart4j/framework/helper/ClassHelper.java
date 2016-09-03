package org.smart4j.framework.helper;

import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.util.ClassUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mcfell on 16/9/3.
 */
public final class ClassHelper {

    private static final Set<Class<?>> CLASS_SET;

    static {
        String basePackage = ConfigHelper.getAppBasePackage();
        CLASS_SET = ClassUtil.getClassSet(basePackage);
    }

    public static Set<Class<?>> getClassSet() {
        return CLASS_SET;
    }

    /**
     * 获取Service注解
     * @return
     */
    public static Set<Class<?>> getServiceSet() {
        Set<Class<?>> serviceSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET) {
            if (cls.isAnnotationPresent(Service.class)) {
                serviceSet.add(cls);
            }
        }
        return serviceSet;
    }

    /**
     * 获取Controller注解
     * @return
     */
    public static Set<Class<?>> getControllerSet() {
        Set<Class<?>> controllerSet = new HashSet<Class<?>>();
        for (Class<?> cls : CLASS_SET) {
            if (cls.isAnnotationPresent(Controller.class)) {
                controllerSet.add(cls);
            }
        }
        return controllerSet;
    }

    /**
     * 获取应用包下的所有Bean类(包含Service/Controller等
     */
    public static Set<Class<?>> getBeanSet() {
        Set<Class<?>> beanSet = new HashSet<Class<?>>();
        beanSet.addAll(getControllerSet());
        beanSet.addAll(getServiceSet());
        return beanSet;
    }
}
