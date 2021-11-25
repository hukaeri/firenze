package com.lxy.firenze.framework.demo.sevice;

import com.lxy.firenze.framework.demo.dto.Student;
import com.lxy.firenze.framework.demo.repository.StudentRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@Named
public class StudentServiceImpl implements StudentService {

    @Inject
    private StudentRepository studentRepository;

    @Override
    public List<Student> all() {
        return studentRepository.all();
    }

    @Override
    public Student create() {
        return studentRepository.create();
    }

    @Override
    public Student get(Integer id) {
        return studentRepository.get(id);
    }
}
