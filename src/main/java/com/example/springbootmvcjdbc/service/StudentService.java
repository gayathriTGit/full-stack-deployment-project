package com.example.springbootmvcjdbc.service;

import com.example.springbootmvcjdbc.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<Student> getAllStudents();
    Student getStudentById(Long id);
    Student createStudent(Student student);
    Student updateStudent(Student student);
    void deleteStudent(Long id);
}
