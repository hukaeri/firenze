package com.lxy.firenze.framework.jaxrs;

import com.lxy.firenze.framework.jaxrs.demo.ScoreResource;
import com.lxy.firenze.framework.jaxrs.demo.StudentsResource;
import org.junit.jupiter.api.Test;

import javax.ws.rs.GET;
import javax.ws.rs.POST;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BearerTest {

    @Test
    public void should_find_getList_method_in_resource() throws NoSuchMethodException {
        RootResourceBearer rootResource = new RootResourceBearer(this.getClass(), "");
        rootResource.register();

        ResourceMethodBearer studentsGetMethod = rootResource.lookup("/students", GET.class);

        assertEquals(studentsGetMethod.data, StudentsResource.class.getMethod("getList"));
        assertNull(studentsGetMethod.invoke());
    }

    @Test
    public void should_find_getOne_method_in_resource() throws NoSuchMethodException {
        RootResourceBearer rootResource = new RootResourceBearer(this.getClass(), "");
        rootResource.register();

        ResourceMethodBearer studentsGetOneMethod = rootResource.lookup("/students/1", GET.class);

        assertEquals(studentsGetOneMethod.data, StudentsResource.class.getMethod("getOne", Integer.class));
        assertEquals(studentsGetOneMethod.invoke(), 1);
    }

    @Test
    public void should_find_post_method_in_resource() throws NoSuchMethodException {
        RootResourceBearer rootResource = new RootResourceBearer(this.getClass(), "");
        rootResource.register();

        ResourceMethodBearer studentsPostMethod = rootResource.lookup("/students", POST.class);

        assertEquals(studentsPostMethod.data, StudentsResource.class.getMethod("post"));
        assertNull(studentsPostMethod.invoke());
    }

    @Test
    public void should_find_getOne_method_in_sub_resource() throws NoSuchMethodException {
        RootResourceBearer rootResource = new RootResourceBearer(this.getClass(), "");
        rootResource.register();

        ResourceMethodBearer scoreGetMethod = rootResource.lookup("/students/1/scores/2", GET.class);

        assertEquals(scoreGetMethod.data, ScoreResource.class.getMethod("getOne"));
        assertEquals(scoreGetMethod.invoke(), 2);
    }

    @Test
    public void should_find_post_method_in_sub_resource() throws NoSuchMethodException {
        RootResourceBearer rootResource = new RootResourceBearer(this.getClass(), "");
        rootResource.register();

        ResourceMethodBearer scorePostMethod = rootResource.lookup("/students/1/scores/1", POST.class);

        assertEquals(scorePostMethod.data, ScoreResource.class.getMethod("post"));
        assertNull(scorePostMethod.invoke());
    }
}

