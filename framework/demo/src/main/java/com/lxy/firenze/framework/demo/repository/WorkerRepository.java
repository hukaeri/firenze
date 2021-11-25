package com.lxy.firenze.framework.demo.repository;

import com.lxy.firenze.framework.demo.dto.Worker;

import java.util.List;

public interface WorkerRepository {

    List<Worker> getAll();

    Worker create();

    Worker get(Integer workerId);
}
