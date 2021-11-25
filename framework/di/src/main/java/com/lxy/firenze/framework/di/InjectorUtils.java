package com.lxy.firenze.framework.di;

import javax.inject.Inject;
import java.lang.reflect.Field;

public class InjectorUtils {

    public static void autowire(DependencyInjector dependencyInjector, Class clazz, Object instance)
            throws IllegalAccessException, InstantiationException {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                field.setAccessible(true);
                Object fieldInstance = dependencyInjector.getBean(field.getType());
                autowire(dependencyInjector, field.getType(), fieldInstance);
                field.set(instance, fieldInstance);
            }
        }
    }
}
