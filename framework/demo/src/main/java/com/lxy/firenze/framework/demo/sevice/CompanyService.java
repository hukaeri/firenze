package com.lxy.firenze.framework.demo.sevice;

import com.lxy.firenze.framework.demo.dto.Company;
import com.lxy.firenze.framework.demo.dto.Worker;

import java.util.List;

public interface CompanyService {

    List<Company> getAll();

    Company create();

    Company get(Integer id);

    List<Worker> getAllWorkers();

    Worker createWorker();

    Worker getWorker(Integer workerId);
}
