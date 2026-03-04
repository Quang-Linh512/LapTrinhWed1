package com.example.studentmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.studentmanager.model.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
}

