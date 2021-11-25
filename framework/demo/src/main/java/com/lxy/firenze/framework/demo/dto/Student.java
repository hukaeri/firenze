package com.lxy.firenze.framework.demo.dto;

import java.util.ArrayList;
import java.util.List;

public class Student {

    private static List<Student> students = new ArrayList<>();

    private String value;

    public Student(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("[%s]value: %s", this.getClass().getSimpleName(), value);
    }

    public static List<Student> all() {
        return students;
    }

    public static Student create() {
        Student company = new Student(Student.class.getSimpleName() + "-" + students.size());
        students.add(company);
        return company;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Student() {
    }
}
