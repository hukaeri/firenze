package com.lxy.firenze.framework.demo;

import com.lxy.firenze.framework.jaxrs.JaxrsApplication;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientResponse;
import io.vertx.core.http.HttpMethod;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(VertxExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApplicationTest {

    @BeforeAll
    static void start() throws InterruptedException {
        JaxrsApplication.start(DemoApplication.class);
        TimeUnit.SECONDS.sleep(3);
    }

    @Test
    @Order(1)
    public void should_create_company(Vertx vertx, VertxTestContext testContext) {
        HttpClient client = vertx.createHttpClient();
        client.request(HttpMethod.POST, 8080, "localhost", "/companies")
                .compose(req -> req.send().compose(HttpClientResponse::body))
                .onComplete(testContext.succeeding(buffer -> testContext.verify(() -> {
                    assertEquals(buffer.toJsonObject().getString("value"), "Company-0");
                    testContext.completeNow();
                })));
    }

    @Test
    public void should_get_companies(Vertx vertx, VertxTestContext testContext) {
        HttpClient client = vertx.createHttpClient();
        client.request(HttpMethod.GET, 8080, "localhost", "/companies")
                .compose(req -> req.send().compose(HttpClientResponse::body))
                .onComplete(testContext.succeeding(buffer -> testContext.verify(() -> {
                    assertEquals(buffer.toJsonArray().getJsonObject(0).getString("value"), "Company-0");
                    testContext.completeNow();
                })));
    }

    @Test
    public void should_get_company(Vertx vertx, VertxTestContext testContext) {
        HttpClient client = vertx.createHttpClient();
        client.request(HttpMethod.GET, 8080, "localhost", "/companies/0")
                .compose(req -> req.send().compose(HttpClientResponse::body))
                .onComplete(testContext.succeeding(buffer -> testContext.verify(() -> {
                    assertEquals(buffer.toJsonObject().getString("value"), "Company-0");
                    testContext.completeNow();
                })));
    }

    @Test
    @Order(1)
    public void should_create_worker(Vertx vertx, VertxTestContext testContext) {
        HttpClient client = vertx.createHttpClient();
        client.request(HttpMethod.POST, 8080, "localhost", "/companies/0/workers")
                .compose(req -> req.send().compose(HttpClientResponse::body))
                .onComplete(testContext.succeeding(buffer -> testContext.verify(() -> {
                    assertEquals(buffer.toJsonObject().getString("value"), "Worker-0");
                    testContext.completeNow();
                })));
    }

    @Test
    public void should_get_workers(Vertx vertx, VertxTestContext testContext) {
        HttpClient client = vertx.createHttpClient();
        client.request(HttpMethod.GET, 8080, "localhost", "/companies/0/workers")
                .compose(req -> req.send().compose(HttpClientResponse::body))
                .onComplete(testContext.succeeding(buffer -> testContext.verify(() -> {
                    assertEquals(buffer.toJsonArray().getJsonObject(0).getString("value"), "Worker-0");
                    testContext.completeNow();
                })));
    }

    @Test
    public void should_get_worker(Vertx vertx, VertxTestContext testContext) {
        HttpClient client = vertx.createHttpClient();
        client.request(HttpMethod.GET, 8080, "localhost", "/companies/0/workers/0")
                .compose(req -> req.send().compose(HttpClientResponse::body))
                .onComplete(testContext.succeeding(buffer -> testContext.verify(() -> {
                    assertEquals(buffer.toJsonObject().getString("value"), "Worker-0");
                    testContext.completeNow();
                })));
    }

    @Test
    @Order(1)
    public void should_create_student(Vertx vertx, VertxTestContext testContext) {
        HttpClient client = vertx.createHttpClient();
        client.request(HttpMethod.POST, 8080, "localhost", "/students")
                .compose(req -> req.send().compose(HttpClientResponse::body))
                .onComplete(testContext.succeeding(buffer -> testContext.verify(() -> {
                    assertEquals(buffer.toJsonObject().getString("value"), "Student-0");
                    testContext.completeNow();
                })));
    }

    @Test
    public void should_get_students(Vertx vertx, VertxTestContext testContext) {
        HttpClient client = vertx.createHttpClient();
        client.request(HttpMethod.GET, 8080, "localhost", "/students")
                .compose(req -> req.send().compose(HttpClientResponse::body))
                .onComplete(testContext.succeeding(buffer -> testContext.verify(() -> {
                    assertEquals(buffer.toJsonArray().getJsonObject(0).getString("value"), "Student-0");
                    testContext.completeNow();
                })));
    }

    @Test
    public void should_get_student(Vertx vertx, VertxTestContext testContext) {
        HttpClient client = vertx.createHttpClient();
        client.request(HttpMethod.GET, 8080, "localhost", "/students/0")
                .compose(req -> req.send().compose(HttpClientResponse::body))
                .onComplete(testContext.succeeding(buffer -> testContext.verify(() -> {
                    assertEquals(buffer.toJsonObject().getString("value"), "Student-0");
                    testContext.completeNow();
                })));
    }

    @Test
    @Order(1)
    public void should_create_score(Vertx vertx, VertxTestContext testContext) {
        HttpClient client = vertx.createHttpClient();
        client.request(HttpMethod.POST, 8080, "localhost", "/students/0/scores/0")
                .compose(req -> req.send().compose(HttpClientResponse::body))
                .onComplete(testContext.succeeding(buffer -> testContext.verify(() -> {
                    assertEquals(buffer.toJsonObject().getString("value"), "Score-0");
                    testContext.completeNow();
                })));
    }

    @Test
    public void should_get_score(Vertx vertx, VertxTestContext testContext) {
        HttpClient client = vertx.createHttpClient();
        client.request(HttpMethod.GET, 8080, "localhost", "/students/0/scores/0")
                .compose(req -> req.send().compose(HttpClientResponse::body))
                .onComplete(testContext.succeeding(buffer -> testContext.verify(() -> {
                    assertEquals(buffer.toJsonObject().getString("value"), "Score-0");
                    testContext.completeNow();
                })));
    }

}
