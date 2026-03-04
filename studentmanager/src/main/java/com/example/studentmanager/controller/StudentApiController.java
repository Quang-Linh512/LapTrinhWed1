package com.example.studentmanager.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.studentmanager.model.Student;
import com.example.studentmanager.service.StudentService;

@RestController
@RequestMapping("/api/students")
@CrossOrigin(origins = "*") // Cho phép frontend gọi API
public class StudentApiController {

    @Autowired
    private StudentService studentService;

    // GET /api/students - Lấy tất cả sinh viên
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    // GET /api/students/{id} - Lấy sinh viên theo ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) {
        Student student = studentService.getStudentById(id);
        if (student != null) {
            return ResponseEntity.ok(student);
        }
        return ResponseEntity.notFound().build();
    }

    // POST /api/students - Thêm sinh viên mới
    @PostMapping
    public ResponseEntity<Map<String, Object>> addStudent(@RequestBody Student student) {
        Map<String, Object> response = new HashMap<>();
        try {
            Student savedStudent = studentService.addStudent(student);
            response.put("success", true);
            response.put("message", "Thêm sinh viên thành công!");
            response.put("data", savedStudent);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // POST /api/students/update/{id} - Cập nhật sinh viên (theo yêu cầu đề)
    @PostMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateStudent(
            @PathVariable int id,
            @RequestBody Student studentDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            Student updatedStudent = studentService.updateStudent(id, studentDetails);
            if (updatedStudent != null) {
                response.put("success", true);
                response.put("message", "Cập nhật sinh viên thành công!");
                response.put("data", updatedStudent);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Không tìm thấy sinh viên với ID: " + id);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // POST /api/students/delete/{id} - Xóa sinh viên (theo yêu cầu đề)
    @PostMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteStudent(@PathVariable int id) {
        Map<String, Object> response = new HashMap<>();
        boolean deleted = studentService.deleteStudent(id);
        if (deleted) {
            response.put("success", true);
            response.put("message", "Xóa sinh viên thành công!");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Không tìm thấy sinh viên với ID: " + id);
            return ResponseEntity.notFound().build();
        }
    }

    // GET /api/students/search?keyword=... - Tìm kiếm sinh viên
    @GetMapping("/search")
    public ResponseEntity<List<Student>> searchStudents(@RequestParam String keyword) {
        List<Student> students = studentService.searchStudents(keyword);
        return ResponseEntity.ok(students);
    }
}
