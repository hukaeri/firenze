package com.lxy.firenze.framework.jaxrs.demo;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/scores/{scoreId}")
public class ScoreResource {

    @PathParam("scoreId")
    Integer scoreId;

    public ScoreResource(Integer scoreId) {
        this.scoreId = scoreId;
    }

    @GET
    public Integer getOne() {
        return scoreId;
    }

    @POST
    public Object post() {
        return null;
    }
}
