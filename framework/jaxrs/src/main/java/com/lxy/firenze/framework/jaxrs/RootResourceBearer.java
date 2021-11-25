package com.lxy.firenze.framework.jaxrs;

import com.lxy.firenze.framework.di.ReflectionUtils;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.Set;

public class RootResourceBearer extends ResourceBearer<Class> {


    public RootResourceBearer(Class data, String path) {
        super(data, path);
        subResources = new ArrayList<>();
    }

    @Override
    public void register() {
        Set<Class<?>> classes = ReflectionUtils.scanPackage(data.getPackage().getName());
        for (Class clazz : classes) {
            if (clazz.isAnnotationPresent(Path.class)) {
                Path pathAnnotation = (Path) clazz.getDeclaredAnnotation(Path.class);
                ResourceClassBearer classBearer = new ResourceClassBearer(clazz, pathAnnotation.value());
                classBearer.register();
                subResources.add(classBearer);
            }
        }
    }
}
