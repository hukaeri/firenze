package com.lxy.firenze.framework.jaxrs;

import com.lxy.firenze.framework.di.DependencyInjector;
import io.vertx.core.Vertx;


public class JaxrsApplication {

    private static JaxrsApplication application;

    private RootResourceBearer rootResource;

    public static void start(Class<?> clazz) {
        DependencyInjector.start(clazz);
        init(clazz);
        startServer();
    }

    private static void init(Class<?> clazz) {
        if (application == null) {
            application = new JaxrsApplication();
            application.register(clazz);
        }
    }

    private void register(Class<?> mainClass) {
        rootResource = new RootResourceBearer(mainClass, "");
        rootResource.register();
    }

    public static RootResourceBearer getRootResource() {
        return application.rootResource;
    }

    private static void startServer() {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new Server());
    }
}
