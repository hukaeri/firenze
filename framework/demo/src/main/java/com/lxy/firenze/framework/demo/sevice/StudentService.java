package com.lxy.firenze.framework.demo.sevice;

import com.lxy.firenze.framework.demo.dto.Student;

import java.util.List;

public interface StudentService {

    List<Student> all();

    Student create();

    Student get(Integer id);
}
