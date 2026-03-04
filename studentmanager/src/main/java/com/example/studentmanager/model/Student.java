package com.example.studentmanager.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sinhvien")
public class Student {
    @Id
    private int id;
    
    private String name;
    private String email;
    private String sdt; // Số điện thoại

    // Default constructor
    public Student() {
    }

    // Constructor without sdt (for backward compatibility)
    public Student(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.sdt = ""; // Default empty phone
    }

    // Constructor with all fields
    public Student(int id, String name, String email, String sdt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.sdt = sdt;
    }

    // Constructor với age (backward compatibility với HelloController)
    public Student(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.email = "";
        this.sdt = "";
    }

    public Student(int id, String name, int age, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.sdt = "";
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    // Backward compatibility methods (for HelloController)
    public int getAge() {
        return 0; // Default age
    }

    public void setAge(int age) {
        // Do nothing, age không có trong database
    }
}