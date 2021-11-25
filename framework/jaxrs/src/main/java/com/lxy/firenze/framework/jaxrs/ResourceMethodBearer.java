package com.lxy.firenze.framework.jaxrs;

import javax.ws.rs.PathParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.stream.Stream;

public class ResourceMethodBearer extends ResourceBearer<Method> {

    private ResourceClassBearer classBearer;

    public ResourceMethodBearer(Method data, String path, ResourceClassBearer classBearer) {
        super(data, path);
        this.classBearer = classBearer;
    }

    public Object invoke() {
        try {
            Object object = classBearer.instantiate();
            Object[] args = resolveArgs();
            return data.invoke(object, args);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
        return data.isAnnotationPresent(annotationClass);
    }

    @Override
    protected boolean match(String url, Class<? extends Annotation> methodAnnotation) {
        String matchedPath = RegexUtils.match(url, this.path);
        if (matchedPath != null && isAnnotationPresent(methodAnnotation)) {
            this.matchedPath = matchedPath;
            return true;
        }
        return false;
    }

    private Object[] resolveArgs() {
        Map<String, String> pathVariableMap = RegexUtils.pathVariableMap(matchedPath, path);
        return Stream.of(data.getParameters())
                .filter(p -> p.isAnnotationPresent(PathParam.class))
                .map(p -> {
                    PathParam pathParam = p.getAnnotation(PathParam.class);
                    String value = pathVariableMap.get(pathParam.value());
                    if (p.getType().isAssignableFrom(Integer.class)) {
                        return Integer.valueOf(value);
                    }
                    return value;
                })
                .toArray(Object[]::new);
    }

}
