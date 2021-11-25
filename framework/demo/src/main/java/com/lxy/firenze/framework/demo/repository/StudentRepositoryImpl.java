package com.lxy.firenze.framework.demo.repository;

import com.lxy.firenze.framework.demo.dto.Student;

import javax.inject.Named;
import java.util.List;

@Named
public class StudentRepositoryImpl implements StudentRepository {

    @Override
    public List<Student> all() {
        return Student.all();
    }

    @Override
    public Student create() {
        return Student.create();
    }

    @Override
    public Student get(Integer id) {
        return Student.all().get(id);
    }
}
