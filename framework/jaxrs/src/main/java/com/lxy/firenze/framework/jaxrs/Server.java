package com.lxy.firenze.framework.jaxrs;

import com.google.common.collect.ImmutableMap;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.Json;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import java.lang.annotation.Annotation;
import java.util.Map;

public class Server extends AbstractVerticle {

    private static Map<HttpMethod, Class<? extends Annotation>> methodMap =
            ImmutableMap.<HttpMethod, Class<? extends Annotation>>builder()
                    .put(HttpMethod.GET, GET.class)
                    .put(HttpMethod.POST, POST.class)
                    .build();

    @Override
    public void start() throws Exception {
        vertx.createHttpServer()
                .requestHandler(this::handler)
                .listen(8080);
    }

    @Override
    public void stop() {
        // TODO
        System.out.println("Shutting down application");
    }

    private void handler(HttpServerRequest request) {
        String url = request.uri();
        Class<? extends Annotation> methodAnnotation = resolveMethod(request.method());

        try {
            ResourceMethodBearer methodBearer = lookUp(url, methodAnnotation);
            if (methodBearer == null) {
                response(request, "{\"code\":\"404\"}");
            } else {
                Object respData = methodBearer.invoke();
                response(request, respData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response(request, String.format("{\"code\":500, \"message\": \"%s\"}", e.getMessage()));
        }
    }

    private ResourceMethodBearer lookUp(String url, Class<? extends Annotation> methodAnnotation) {
        RootResourceBearer rootResource = JaxrsApplication.getRootResource();
        return rootResource.lookup(url, methodAnnotation);
    }

    private void clearCache() {
        RootResourceBearer rootResource = JaxrsApplication.getRootResource();
        rootResource.clearCache();
    }

    private Class<? extends Annotation> resolveMethod(HttpMethod method) {
        return methodMap.get(method);
    }

    private void response(HttpServerRequest request, Object data) {
        clearCache();
        String resp = data instanceof String ? (String) data : Json.encodePrettily(data);
        request.response()
                .putHeader("content-type", "application/json")
                .end(resp);
    }
}
