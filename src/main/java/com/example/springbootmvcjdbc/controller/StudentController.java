package com.example.springbootmvcjdbc.controller;

import com.example.springbootmvcjdbc.model.Student;
import com.example.springbootmvcjdbc.service.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.*;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    //List all students
    @GetMapping
    public String listStudents(Model model){
        System.out.println("*** CALLING listStudents() method ***");
        List<Student> studentList = studentService.getAllStudents();
        System.out.println("Students found: " + studentList.size());
        model.addAttribute("students", studentList);
        return "students/list";
    }

    // Show form for creating new student
    @GetMapping("/new")
    public String showCreateForm(Model model){
        System.out.println("*** CALLING showCreateForm() method ***");
        model.addAttribute( "student", new Student());
        return "students/form";
    }

    // Show form for editing existing student
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model){
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return "redirect:/students";
        }
        model.addAttribute("student", student);
        return "students/form";
    }

    // Handle form submission for create/update
    @PostMapping("/save")
    public String saveStudent(@ModelAttribute Student student, RedirectAttributes redirectAttributes){
        try{
            if (student.getId() == null){
                studentService.createStudent(student);
                redirectAttributes.addFlashAttribute("successMessage", "Student created successfully!");
            } else {
                studentService.updateStudent(student);
                redirectAttributes.addFlashAttribute("successMessage", "Student updated successfully!");
            }
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving student: " + e.getMessage());
        }
        return "redirect:/students";
    }

    // Delete student
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            studentService.deleteStudent(id);
            redirectAttributes.addFlashAttribute("successMessage", "Student deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting student: " + e.getMessage());
        }
        return "redirect:/students";
    }

    // Home page redirect
    @GetMapping("/")
    public String home() {
        return "redirect:/students";
    }

}
