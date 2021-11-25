package com.lxy.firenze.framework.jaxrs;

import com.lxy.firenze.framework.di.DependencyInjector;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ResourceClassBearer extends ResourceBearer<Class> {

    private static List<Class<? extends Annotation>> httpMethodAnnotations = Arrays.asList(GET.class, POST.class);

    public ResourceClassBearer(Class data, String path) {
        super(data, path);
    }

    public Object instantiate() throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        if (path != null && matchedPath != null) {
            Map<String, String> pathVariableMap = RegexUtils.pathVariableMap(matchedPath, path);
            Field[] fields = data.getDeclaredFields();
            Object[] pathParams = Stream.of(fields)
                    .filter(f -> f.isAnnotationPresent(PathParam.class))
                    .map(p -> {
                        PathParam pathParam = p.getAnnotation(PathParam.class);
                        String value = pathVariableMap.get(pathParam.value());
                        if (p.getType().isAssignableFrom(Integer.class)) {
                            return Integer.valueOf(value);
                        }
                        return value;
                    })
                    .toArray(Object[]::new);
            Class[] constructorParamClasses = IntStream.range(0, pathParams.length)
                    .mapToObj(i -> fields[i].getType())
                    .toArray(Class[]::new);
            Constructor constructor = data.getConstructor(constructorParamClasses);
            Object instance = constructor.newInstance(pathParams);
            for (Field field : fields) {
                if (field.isAnnotationPresent(Inject.class)) {
                    field.setAccessible(true);
                    field.set(instance, DependencyInjector.getInstance(field.getType()));
                }
            }
            return instance;
        }
        throw new RuntimeException("instantiate not here");
    }

    @Override
    public void register() {
        Method[] methods = data.getDeclaredMethods();
        if (methods.length > 0) {
            subResources = new ArrayList<>(methods.length);
            for (Method method : methods) {
                if (isHttpMethodAnnotationPresent(method)) {
                    ResourceMethodBearer methodBearer;
                    if (method.isAnnotationPresent(Path.class)) {
                        Path pathAnnotation = method.getDeclaredAnnotation(Path.class);
                        methodBearer = new ResourceMethodBearer(method, pathAnnotation.value(), this);
                    } else {
                        methodBearer = new ResourceMethodBearer(method, "", this);
                    }
                    // methodBearer.register();
                    subResources.add(methodBearer);
                } else if (method.isAnnotationPresent(Path.class)) {
                    Path pathAnnotation = method.getDeclaredAnnotation(Path.class);
                    ResourceClassBearer classBearer = new ResourceClassBearer(method.getReturnType(), pathAnnotation.value());
                    classBearer.register();
                    subResources.add(classBearer);
                }
            }
        }
    }

    private Boolean isHttpMethodAnnotationPresent(Method method) {
        return httpMethodAnnotations.stream()
                .anyMatch(a -> method.isAnnotationPresent(a));
    }

}
