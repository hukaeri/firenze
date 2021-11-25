package com.lxy.firenze.framework.demo.repository;

import com.lxy.firenze.framework.demo.dto.Worker;

import javax.inject.Named;
import java.util.List;

@Named
public class WorkerRepositoryImpl implements WorkerRepository {

    @Override
    public List<Worker> getAll() {
        return Worker.all();
    }

    @Override
    public Worker create() {
        return Worker.create();
    }

    @Override
    public Worker get(Integer workerId) {
        return Worker.all().get(workerId);
    }
}
