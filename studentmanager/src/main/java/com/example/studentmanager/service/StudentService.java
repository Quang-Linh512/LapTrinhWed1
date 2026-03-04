package com.example.studentmanager.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.studentmanager.model.Student;
import com.example.studentmanager.repository.StudentRepository;

@Service
public class StudentService {
    
    @Autowired
    private StudentRepository repository;

    // READ - Lấy tất cả sinh viên
    public List<Student> getAllStudents() {
        return repository.findAll();
    }

    // READ - Lấy sinh viên theo ID
    public Student getStudentById(int id) {
        Optional<Student> student = repository.findById(id);
        return student.orElse(null);
    }

    // CREATE - Thêm sinh viên mới
    public Student addStudent(Student student) {
        return repository.save(student);
    }

    // UPDATE - Cập nhật sinh viên
    public Student updateStudent(int id, Student studentDetails) {
        Optional<Student> optionalStudent = repository.findById(id);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            student.setName(studentDetails.getName());
            student.setEmail(studentDetails.getEmail());
            student.setSdt(studentDetails.getSdt());
            return repository.save(student);
        }
        return null;
    }

    // DELETE - Xóa sinh viên
    public boolean deleteStudent(int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    // SEARCH - Tìm kiếm sinh viên theo tên
    public List<Student> searchStudents(String keyword) {
        return repository.findAll().stream()
            .filter(s -> s.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                       s.getEmail().toLowerCase().contains(keyword.toLowerCase()))
            .toList();
    }
}

