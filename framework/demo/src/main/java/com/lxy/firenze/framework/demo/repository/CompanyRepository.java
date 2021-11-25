package com.lxy.firenze.framework.demo.repository;

import com.lxy.firenze.framework.demo.dto.Company;

import java.util.List;

public interface CompanyRepository {

    List<Company> getAll();

    Company create();

    Company get(Integer id);
}
