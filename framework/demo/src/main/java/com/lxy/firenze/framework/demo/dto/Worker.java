package com.lxy.firenze.framework.demo.dto;

import java.util.ArrayList;
import java.util.List;

public class Worker {

    private static List<Worker> workers = new ArrayList<>();

    private String value;

    public Worker(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("[%s]value: %s", this.getClass().getSimpleName(), value);
    }

    public static List<Worker> all() {
        return workers;
    }

    public static Worker create() {
        Worker company = new Worker(Worker.class.getSimpleName() + "-" + workers.size());
        workers.add(company);
        return company;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Worker() {
    }
}
