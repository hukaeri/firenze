package com.lxy.firenze.framework.demo.client;

import com.lxy.firenze.framework.demo.dto.Company;
import com.lxy.firenze.framework.demo.dto.Worker;
import com.lxy.firenze.framework.demo.sevice.CompanyService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.List;

@Path("/companies")
public class CompaniesResource {

    @Inject
    private CompanyService companyService;

    @GET
    public List<Company> getAll() {
        return companyService.getAll();
    }

    @POST
    public Company create() {
        return companyService.create();
    }

    @GET
    @Path("/{id}")
    public Company getOne(@PathParam("id") Integer id) {
        return companyService.get(id);
    }

    @GET
    @Path("/{id}/workers")
    public List<Worker> getAllWorkers() {
        return companyService.getAllWorkers();
    }

    @POST
    @Path("/{id}/workers")
    public Worker createWorker() {
        return companyService.createWorker();
    }

    @GET
    @Path("/{companyId}/workers/{workerId}")
    public Worker getOneWorker(@PathParam("workerId") Integer workerId) {
        return companyService.getWorker(workerId);
    }

}
