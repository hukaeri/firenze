package com.lxy.firenze.framework.jaxrs.demo;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path("/students")
public class StudentsResource {

    public StudentsResource() {
    }

    @GET
    public List getList() {
        return null;
    }

    @POST
    public Object post() {
        return null;
    }

    @GET
    @Path("/{id}")
    public Integer getOne(@PathParam("id") Integer id) {
        return id;
    }

    @Path("/{id}/scores/{scoreId}")
    public ScoreResource getSubResource(@PathParam("scoreId") Integer scoreId) {
        return new ScoreResource(scoreId);
    }
}
