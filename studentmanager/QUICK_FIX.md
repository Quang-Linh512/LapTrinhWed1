# 🚀 HƯỚNG DẪN SỬA LỖI KẾT NỐI NHANH

## ✅ ĐÃ CẬP NHẬT:

1. **Connection String** - Đổi sang format chuẩn: `instanceName=SQLEXPRESS`
2. **DatabaseConnectionTest** - Tự động tìm bảng `students` hoặc `sinhvien`
3. **Model Student** - Đã map với bảng `sinhvien` (id, name, email, sdt)

## 🔧 CÁC BƯỚC KIỂM TRA:

### Bước 1: Kiểm tra SQL Server đang chạy
- Mở **SQL Server Configuration Manager**
- Kiểm tra **SQL Server (SQLEXPRESS)** đang **Running**

### Bước 2: Test kết nối trong SSMS
1. Mở **SQL Server Management Studio**
2. Connect với:
   - Server: `localhost\SQLEXPRESS`
   - Authentication: **SQL Server Authentication**
   - Login: `sa`
   - Password: `123456`
3. Nếu kết nối được → OK!

### Bước 3: Kiểm tra database và bảng
```sql
USE school1234;
GO

-- Kiểm tra bảng sinhvien
SELECT * FROM sinhvien;
GO
```

### Bước 4: Nếu user sa chưa có quyền
Chạy script này trong SSMS:
```sql
USE school1234;
GO

-- Tạo user nếu chưa có
IF NOT EXISTS (SELECT * FROM sys.database_principals WHERE name = 'sa')
BEGIN
    CREATE USER [sa] FOR LOGIN [sa];
END
GO

-- Cấp quyền
ALTER ROLE db_datareader ADD MEMBER [sa];
ALTER ROLE db_datawriter ADD MEMBER [sa];
GO
```

### Bước 5: Restart Spring Boot
1. Dừng ứng dụng
2. Chạy lại
3. Xem console - sẽ có log từ `DatabaseConnectionTest`
4. Truy cập: `http://localhost:8080/students`

## 🔄 NẾU VẪN LỖI - THỬ CÁC CÁCH SAU:

### Cách 1: Đổi connection string (trong application.properties)
Uncomment "Cách 2":
```properties
spring.datasource.url=jdbc:sqlserver://localhost\\SQLEXPRESS:1433;encrypt=true;trustServerCertificate=true;databaseName=school1234
```

### Cách 2: Bỏ instance name
Uncomment "Cách 3":
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;encrypt=true;trustServerCertificate=true;databaseName=school1234
```

### Cách 3: Dùng tên máy tính
Uncomment "Cách 4" và thay `LÊQUANGLINH` bằng tên máy của bạn:
```properties
spring.datasource.url=jdbc:sqlserver://TENMAY\\SQLEXPRESS:1433;encrypt=true;trustServerCertificate=true;databaseName=school1234
```

## 📊 KIỂM TRA LOGS:

Khi chạy ứng dụng, xem console output:
- ✅ Nếu thấy "KẾT NỐI THÀNH CÔNG!" → Database OK
- ❌ Nếu thấy "LỖI KẾT NỐI!" → Xem chi tiết lỗi và làm theo hướng dẫn

## 🎯 TEST ENDPOINT:

Sau khi chạy ứng dụng, test:
- `http://localhost:8080/test/db-connection` - Test kết nối
- `http://localhost:8080/students` - Xem danh sách sinh viên
