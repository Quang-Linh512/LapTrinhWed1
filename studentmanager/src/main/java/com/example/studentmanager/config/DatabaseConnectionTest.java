package com.example.studentmanager.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConnectionTest implements CommandLineRunner {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n========================================");
        System.out.println("🔍 KIỂM TRA KẾT NỐI SQL SERVER");
        System.out.println("========================================\n");
        
        System.out.println("📋 Thông tin kết nối:");
        System.out.println("   URL: " + url);
        System.out.println("   Username: " + username);
        System.out.println("   Password: " + (password != null ? "***" : "null"));
        System.out.println();

        try {
            // Load driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("✅ SQL Server Driver đã được load thành công!");

            // Test connection
            System.out.println("🔄 Đang thử kết nối...");
            Connection connection = DriverManager.getConnection(url, username, password);
            
            if (connection != null && !connection.isClosed()) {
                System.out.println("✅ KẾT NỐI THÀNH CÔNG!\n");
                
                // Test query
                Statement statement = connection.createStatement();
                
                // Kiểm tra database có tồn tại không
                ResultSet rs = statement.executeQuery("SELECT DB_NAME() AS CurrentDatabase");
                if (rs.next()) {
                    System.out.println("📊 Database hiện tại: " + rs.getString("CurrentDatabase"));
                }
                
                // Kiểm tra các bảng có tồn tại không (students hoặc sinhvien)
                rs = statement.executeQuery(
                    "SELECT TABLE_NAME " +
                    "FROM INFORMATION_SCHEMA.TABLES " +
                    "WHERE TABLE_NAME IN ('students', 'sinhvien')"
                );
                String tableName = null;
                if (rs.next()) {
                    tableName = rs.getString("TABLE_NAME");
                    System.out.println("✅ Tìm thấy bảng: '" + tableName + "'");
                    
                    // Đếm số lượng sinh viên
                    rs = statement.executeQuery("SELECT COUNT(*) AS StudentCount FROM " + tableName);
                    if (rs.next()) {
                        int studentCount = rs.getInt("StudentCount");
                        System.out.println("👥 Số lượng sinh viên: " + studentCount);
                        
                        if (studentCount > 0) {
                            System.out.println("\n📋 Danh sách sinh viên:");
                            rs = statement.executeQuery("SELECT * FROM " + tableName);
                            
                            // Kiểm tra các cột có trong bảng
                            ResultSet columns = statement.executeQuery(
                                "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS " +
                                "WHERE TABLE_NAME = '" + tableName + "'"
                            );
                            boolean hasAge = false, hasSdt = false;
                            while (columns.next()) {
                                String colName = columns.getString("COLUMN_NAME").toLowerCase();
                                if (colName.equals("age")) hasAge = true;
                                if (colName.equals("sdt")) hasSdt = true;
                            }
                            
                            if (hasSdt) {
                                System.out.println("   ID | Tên                | Email              | SĐT");
                                System.out.println("   ---|--------------------|--------------------|-------------------");
                                while (rs.next()) {
                                    System.out.printf("   %-3d| %-18s| %-18s| %s%n",
                                        rs.getInt("id"),
                                        rs.getString("name"),
                                        rs.getString("email") != null ? rs.getString("email") : "",
                                        rs.getString("sdt") != null ? rs.getString("sdt") : "");
                                }
                            } else if (hasAge) {
                                System.out.println("   ID | Tên                | Tuổi | Email");
                                System.out.println("   ---|--------------------|------|-------------------");
                                while (rs.next()) {
                                    System.out.printf("   %-3d| %-18s| %-4d | %s%n",
                                        rs.getInt("id"),
                                        rs.getString("name"),
                                        rs.getInt("age"),
                                        rs.getString("email") != null ? rs.getString("email") : "");
                                }
                            } else {
                                System.out.println("   ID | Tên                | Email");
                                System.out.println("   ---|--------------------|-------------------");
                                while (rs.next()) {
                                    System.out.printf("   %-3d| %-18s| %s%n",
                                        rs.getInt("id"),
                                        rs.getString("name"),
                                        rs.getString("email") != null ? rs.getString("email") : "");
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("⚠️  Không tìm thấy bảng 'students' hoặc 'sinhvien'!");
                    System.out.println("   → Vui lòng tạo bảng trong database!");
                }
                
                statement.close();
                connection.close();
                System.out.println("\n✅ Kết nối đã được đóng an toàn!");
                
            } else {
                System.out.println("❌ Kết nối thất bại!");
            }
            
        } catch (Exception e) {
            System.out.println("❌ LỖI KẾT NỐI!");
            System.out.println("   Chi tiết: " + e.getMessage());
            System.out.println("\n💡 Hướng dẫn khắc phục:");
            System.out.println("   1. Kiểm tra SQL Server đã chạy chưa");
            System.out.println("   2. Kiểm tra database 'school' đã tạo chưa");
            System.out.println("   3. Kiểm tra username/password trong application.properties");
            System.out.println("   4. Kiểm tra port 1433 có đang mở không");
            if (e.getMessage().contains("instanceName") || e.getMessage().contains("SQLEXPRESS")) {
                System.out.println("   5. Nếu dùng SQL Express, thêm instanceName=SQLEXPRESS vào URL");
            }
            e.printStackTrace();
        }
        
        System.out.println("\n========================================\n");
    }
}

