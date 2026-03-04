package com.example.studentmanager.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.studentmanager.model.Student;

@RestController
@RequestMapping("/api")
public class HelloController {
    //Bài 1
    @GetMapping("/hello")
    public String hello() {
        return "Hello Spring Boot";
    }
    //Bài 2
    @GetMapping("/greet")
    public String greet(@RequestParam String name) {
        return "Xin chào " + name;
    }
    @GetMapping("/students/search")
    public String search(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") int page) {
        return "keyword=" + keyword + ", page=" + page;
    }

    //Bài 3
    @GetMapping("/students/{id}")
    public String getStudent(@PathVariable int id) {
         return "Sinh viên có mã: " + id;
    }

    //Bài 4
    @GetMapping("/student")
    public Student getStudent() {
        return new Student(1, "Le Quang Linh", 20, "linh@gmail.com");
    }

    //Bài 5 - Đổi endpoint để tránh xung đột với StudentController
    @GetMapping("/api/students")
    public List<Student> getStudents() {
        List<Student> list = new ArrayList<>();
        list.add(new Student(1, "A", 20, "a@gmail.com"));
        list.add(new Student(2, "B", 21, "b@gmail.com"));
        return list;
    }
}