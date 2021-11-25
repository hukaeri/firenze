package com.lxy.firenze.framework.demo.sevice;

import com.lxy.firenze.framework.demo.dto.Company;
import com.lxy.firenze.framework.demo.dto.Worker;
import com.lxy.firenze.framework.demo.repository.CompanyRepository;
import com.lxy.firenze.framework.demo.repository.WorkerRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
public class CompanyServiceImpl implements CompanyService {

    @Inject
    private CompanyRepository companyRepository;

    @Inject
    private WorkerRepository workerRepository;

    @Override
    public List<Company> getAll() {
        return companyRepository.getAll();
    }

    @Override
    public Company create() {
        return companyRepository.create();
    }

    @Override
    public Company get(Integer id) {
        return companyRepository.get(id);
    }

    @Override
    public List<Worker> getAllWorkers() {
        return workerRepository.getAll();
    }

    @Override
    public Worker createWorker() {
        return workerRepository.create();
    }

    @Override
    public Worker getWorker(Integer workerId) {
        return workerRepository.get(workerId);
    }
}
