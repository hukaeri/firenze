package com.lxy.firenze.framework.demo.client;

import com.lxy.firenze.framework.demo.dto.Score;
import com.lxy.firenze.framework.demo.sevice.ScoreService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/scores/{scoreId}")
public class ScoreResource {

    @PathParam("scoreId")
    Integer id;

    @Inject
    ScoreService scoreService;

    public ScoreResource(Integer id) {
        this.id = id;
    }

    @POST
    public Score create() {
        return scoreService.create();
    }

    @GET
    public Score getOne() {
        return scoreService.get(id);
    }

}
