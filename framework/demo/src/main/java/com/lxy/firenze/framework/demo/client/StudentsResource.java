package com.lxy.firenze.framework.demo.client;

import com.lxy.firenze.framework.demo.dto.Student;
import com.lxy.firenze.framework.demo.sevice.StudentService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path("/students")
public class StudentsResource {

    @Inject
    private StudentService studentService;

    @GET
    public List<Student> getAll() {
        return studentService.all();
    }

    @POST
    public Student create() {
        return studentService.create();
    }

    @GET
    @Path("/{id}")
    public Student getOne(@PathParam("id") Integer id) {
        return studentService.get(id);
    }

    @Path("/{id}/scores/{scoreId}")
    public ScoreResource getScoreResource(@PathParam("scoreId") Integer scoreId) {
        return new ScoreResource(scoreId);
    }

}
