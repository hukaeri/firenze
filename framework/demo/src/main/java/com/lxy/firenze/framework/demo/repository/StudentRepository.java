package com.lxy.firenze.framework.demo.repository;

import com.lxy.firenze.framework.demo.dto.Student;

import java.util.List;

public interface StudentRepository {

    List<Student> all();

    Student create();

    Student get(Integer id);
}
