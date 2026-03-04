package com.example.studentmanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.studentmanager.model.Student;
import com.example.studentmanager.service.StudentService;

@Controller
public class StudentController {
    
    @Autowired
    private StudentService studentService;

    // GET /students - Hiển thị danh sách sinh viên
    @GetMapping("/students")
    public String listStudents(Model model, @RequestParam(required = false) String search) {
        try {
            List<Student> students;
            if (search != null && !search.trim().isEmpty()) {
                students = studentService.searchStudents(search);
                model.addAttribute("searchKeyword", search);
            } else {
                students = studentService.getAllStudents();
            }
            model.addAttribute("students", students);
            return "students";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi kết nối database: " + e.getMessage());
            model.addAttribute("students", List.of());
            return "students";
        }
    }

    // GET /students/add - Hiển thị form thêm sinh viên
    @GetMapping("/students/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("isEdit", false);
        return "student-form";
    }

    // POST /students/add - Xử lý thêm sinh viên
    @PostMapping("/students/add")
    public String addStudent(@ModelAttribute Student student, RedirectAttributes redirectAttributes) {
        try {
            studentService.addStudent(student);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm sinh viên thành công!");
            return "redirect:/students";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            return "redirect:/students/add";
        }
    }

    // GET /students/edit/{id} - Hiển thị form sửa sinh viên
    @GetMapping("/students/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        Student student = studentService.getStudentById(id);
        if (student != null) {
            model.addAttribute("student", student);
            model.addAttribute("isEdit", true);
            return "student-form";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sinh viên với ID: " + id);
            return "redirect:/students";
        }
    }

    // POST /students/edit/{id} - Xử lý cập nhật sinh viên
    @PostMapping("/students/edit/{id}")
    public String updateStudent(@PathVariable int id, @ModelAttribute Student student, RedirectAttributes redirectAttributes) {
        try {
            Student updated = studentService.updateStudent(id, student);
            if (updated != null) {
                redirectAttributes.addFlashAttribute("successMessage", "Cập nhật sinh viên thành công!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sinh viên với ID: " + id);
            }
            return "redirect:/students";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            return "redirect:/students/edit/" + id;
        }
    }

    // GET /students/delete/{id} - Xóa sinh viên
    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            boolean deleted = studentService.deleteStudent(id);
            if (deleted) {
                redirectAttributes.addFlashAttribute("successMessage", "Xóa sinh viên thành công!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sinh viên với ID: " + id);
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
        }
        return "redirect:/students";
    }
}

