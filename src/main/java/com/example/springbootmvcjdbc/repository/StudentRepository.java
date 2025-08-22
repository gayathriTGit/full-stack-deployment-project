package com.example.springbootmvcjdbc.repository;

import com.example.springbootmvcjdbc.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {

    List<Student> findAll();
    Student findById(Long id);
    Student add(Student student);
    Student update(Student student);
    void deleteById(Long id);

}
