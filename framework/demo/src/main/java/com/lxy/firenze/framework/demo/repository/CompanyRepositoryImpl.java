package com.lxy.firenze.framework.demo.repository;

import com.lxy.firenze.framework.demo.dto.Company;

import javax.inject.Named;
import java.util.List;

@Named
public class CompanyRepositoryImpl implements CompanyRepository {

    @Override
    public List<Company> getAll() {
        return Company.all();
    }

    @Override
    public Company create() {
        return Company.create();
    }

    @Override
    public Company get(Integer id) {
        return Company.all().get(id);
    }
}
