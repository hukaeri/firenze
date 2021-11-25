package com.lxy.firenze.framework.demo.dto;

import java.util.ArrayList;
import java.util.List;

public class Company {

    private static List<Company> companies = new ArrayList<>();

    private String value;

    public Company(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("[%s]value: %s", this.getClass().getSimpleName(), value);
    }

    public static List<Company> all() {
        return companies;
    }

    public static Company create() {
        Company company = new Company(Company.class.getSimpleName() + "-" + companies.size());
        companies.add(company);
        return company;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Company() {
    }
}
