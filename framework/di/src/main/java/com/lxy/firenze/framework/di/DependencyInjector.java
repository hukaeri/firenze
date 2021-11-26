package com.lxy.firenze.framework.di;

import javax.inject.Named;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class DependencyInjector {

    private static DependencyInjector dependencyInjector;

    private Map<Class, Class> implIfcMap;

    private Map<Class, Object> instanceMap;

    private DependencyInjector() {
        implIfcMap = new HashMap<>();
        instanceMap = new HashMap<>();
    }

    private void init(Class<?> mainClass) {
        Collection<Class<?>> allScopes = ReflectionUtils.scanPackage(mainClass.getPackage().getName());
        allScopes.stream()
                .filter(clazz -> clazz.isAnnotationPresent(Named.class))
                .forEach(this::buildImplIfcMap);
        allScopes.stream()
                .filter(clazz -> clazz.isAnnotationPresent(Named.class))
                .forEach(this::instantiate);
    }

    private void buildImplIfcMap(Class clazz) {
        Class[] interfaces = clazz.getInterfaces();
        if (interfaces.length == 0) {
            implIfcMap.put(clazz, clazz);
        } else {
            Stream.of(interfaces)
                    .forEach(ifc -> implIfcMap.put(clazz, ifc));
        }
    }

    private void instantiate(Class clazz) {
        try {
            if (!instanceMap.containsKey(clazz)) {
                instanceMap.put(clazz, clazz.newInstance());
            }
            Object instance = instanceMap.get(clazz);
            InjectorUtils.autowire(this, clazz, instance);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void start(Class<?> mainClass) {
        if (dependencyInjector == null) {
            dependencyInjector = new DependencyInjector();
            dependencyInjector.init(mainClass);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> clazz) throws IllegalAccessException, InstantiationException {
        Optional<Class> implOptional = implIfcMap.entrySet().stream()
                .filter(entry -> clazz.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst();

        if (!implOptional.isPresent()) {
            throw new RuntimeException(String.format("impl bean %s not found", clazz.getName()));
        }

        Class implClass = implOptional.get();
        if (!instanceMap.containsKey(implClass)) {
            instanceMap.put(implClass, implClass.newInstance());
        }
        return (T) instanceMap.get(implClass);
    }

    public static <T> T getInstance(Class<T> clazz) {
        try {
            return dependencyInjector.getBean(clazz);
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

}

