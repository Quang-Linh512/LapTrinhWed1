# 🔧 HƯỚNG DẪN KHẮC PHỤC LỖI KẾT NỐI DATABASE

## ❌ Lỗi hiện tại:
**"Could not open JPA EntityManager for transaction"**

## ✅ CÁC BƯỚC KHẮC PHỤC:

### 1️⃣ KIỂM TRA SQL SERVER ĐÃ CHẠY CHƯA

**Cách kiểm tra:**
- Mở **SQL Server Configuration Manager**
- Vào **SQL Server Services**
- Kiểm tra **SQL Server (MSSQLSERVER)** hoặc **SQL Server (SQLEXPRESS)** đang **Running**

**Nếu chưa chạy:**
- Click phải → **Start**

---

### 2️⃣ KIỂM TRA PORT SQL SERVER

**Mặc định:** Port 1433

**Cách kiểm tra:**
- Mở **SQL Server Configuration Manager**
- Vào **SQL Server Network Configuration** → **Protocols for MSSQLSERVER**
- Click phải **TCP/IP** → **Properties** → Tab **IP Addresses**
- Kiểm tra **TCP Port** = **1433**
- Đảm bảo **Enabled** = **Yes**

**Nếu dùng SQL Server Express:**
- Port có thể là **1433** hoặc **SQLEXPRESS**
- Thử thay đổi connection string:
  ```
  jdbc:sqlserver://localhost:1433;instanceName=SQLEXPRESS;databaseName=school;encrypt=true;trustServerCertificate=true
  ```

---

### 3️⃣ TẠO DATABASE VÀ BẢNG

**Cách 1: Dùng SQL Script (Khuyên dùng)**
1. Mở **SQL Server Management Studio (SSMS)**
2. Kết nối với SQL Server (Server name: `localhost` hoặc `.` hoặc `localhost\SQLEXPRESS`)
3. Mở file `setup_database.sql` trong thư mục project
4. Copy toàn bộ nội dung và chạy trong SSMS
5. Kiểm tra kết quả

**Cách 2: Chạy thủ công**
```sql
-- Tạo database
CREATE DATABASE school;
GO

USE school;
GO

-- Tạo bảng
CREATE TABLE students (
    id INT PRIMARY KEY,
    name NVARCHAR(100),
    age INT,
    email NVARCHAR(100)
);

-- Thêm dữ liệu
INSERT INTO students VALUES
(1, N'Nguyễn Văn A', 20, 'a@gmail.com'),
(2, N'Trần Thị B', 21, 'b@gmail.com'),
(3, N'Lê Văn C', 19, 'c@gmail.com');
```

---

### 4️⃣ KIỂM TRA USERNAME VÀ PASSWORD

**Mở file:** `src/main/resources/application.properties`

**Kiểm tra:**
```properties
spring.datasource.username=sa
spring.datasource.password=123456
```

**Nếu username/password khác:**
- Thay đổi trong `application.properties`
- Hoặc tạo user mới trong SQL Server

**Cách tạo user mới (nếu cần):**
```sql
-- Trong SSMS, chạy:
CREATE LOGIN studentuser WITH PASSWORD = 'yourpassword';
USE school;
CREATE USER studentuser FOR LOGIN studentuser;
ALTER ROLE db_datareader ADD MEMBER studentuser;
ALTER ROLE db_datawriter ADD MEMBER studentuser;
```

---

### 5️⃣ KIỂM TRA SQL SERVER AUTHENTICATION

**Cách bật SQL Server Authentication:**
1. Mở **SSMS**
2. Click phải vào Server → **Properties**
3. Tab **Security**
4. Chọn **SQL Server and Windows Authentication mode**
5. Click **OK**
6. **Restart SQL Server**

---

### 6️⃣ KIỂM TRA FIREWALL

**Nếu dùng Windows Firewall:**
- Cho phép port 1433 qua firewall
- Hoặc tắt firewall tạm thời để test

---

### 7️⃣ TEST KẾT NỐI

**Cách test nhanh:**
1. Mở **SSMS**
2. Server name: `localhost` hoặc `.`
3. Authentication: **SQL Server Authentication**
4. Login: `sa`
5. Password: `123456` (hoặc password của bạn)
6. Click **Connect**

**Nếu kết nối thành công:** Database đã sẵn sàng!

**Nếu không kết nối được:** Kiểm tra lại các bước trên.

---

### 8️⃣ CẤU HÌNH CHO SQL SERVER EXPRESS

**Nếu dùng SQL Server Express, thử cấu hình này:**

Trong `application.properties`:
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;instanceName=SQLEXPRESS;databaseName=school;encrypt=true;trustServerCertificate=true
```

Hoặc:
```properties
spring.datasource.url=jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=school;encrypt=true;trustServerCertificate=true
```

---

## 🔍 KIỂM TRA LOGS

**Xem logs trong console khi chạy ứng dụng:**
- Tìm dòng có chứa "SQL" hoặc "Connection"
- Lỗi sẽ hiển thị chi tiết nguyên nhân

---

## ✅ SAU KHI SỬA XONG:

1. **Restart Spring Boot Application**
2. Truy cập: `http://localhost:8080/students`
3. Nếu thành công, sẽ thấy bảng danh sách sinh viên!

---

## 📞 NẾU VẪN LỖI:

Kiểm tra lại:
- ✅ SQL Server đã chạy?
- ✅ Database "school" đã tạo?
- ✅ Bảng "students" đã tạo?
- ✅ Username/password đúng?
- ✅ Port 1433 mở?
- ✅ SQL Server Authentication đã bật?

