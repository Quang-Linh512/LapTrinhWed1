package com.example.studentmanager.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class DatabaseTestController {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @GetMapping("/db-connection")
    public Map<String, Object> testConnection() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // Load driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            result.put("driverLoaded", true);
            
            // Test connection
            Connection connection = DriverManager.getConnection(url, username, password);
            result.put("connected", true);
            result.put("message", "Kết nối thành công!");
            
            // Test query
            Statement statement = connection.createStatement();
            
            // Kiểm tra database
            ResultSet rs = statement.executeQuery("SELECT DB_NAME() AS CurrentDatabase");
            if (rs.next()) {
                result.put("database", rs.getString("CurrentDatabase"));
            }
            
            // Kiểm tra bảng students
            rs = statement.executeQuery(
                "SELECT COUNT(*) AS TableCount " +
                "FROM INFORMATION_SCHEMA.TABLES " +
                "WHERE TABLE_NAME = 'students'"
            );
            if (rs.next()) {
                int tableCount = rs.getInt("TableCount");
                result.put("tableExists", tableCount > 0);
                
                if (tableCount > 0) {
                    // Đếm số lượng sinh viên
                    rs = statement.executeQuery("SELECT COUNT(*) AS StudentCount FROM students");
                    if (rs.next()) {
                        result.put("studentCount", rs.getInt("StudentCount"));
                    }
                } else {
                    result.put("warning", "Bảng 'students' chưa tồn tại! Vui lòng chạy setup_database.sql");
                }
            }
            
            statement.close();
            connection.close();
            
        } catch (Exception e) {
            result.put("connected", false);
            result.put("error", e.getMessage());
            result.put("errorType", e.getClass().getSimpleName());
            result.put("message", "Kết nối thất bại!");
        }
        
        return result;
    }
}

