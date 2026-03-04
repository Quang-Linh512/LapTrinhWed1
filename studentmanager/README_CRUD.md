# 📚 HƯỚNG DẪN SỬ DỤNG HỆ THỐNG QUẢN LÝ SINH VIÊN

## 🎯 TÍNH NĂNG

### ✅ Website Quản lý (Thymeleaf)
- ✅ Xem danh sách sinh viên
- ✅ Thêm sinh viên mới
- ✅ Sửa thông tin sinh viên
- ✅ Xóa sinh viên
- ✅ Tìm kiếm sinh viên theo tên/email

### ✅ REST API
- ✅ GET `/api/students` - Lấy tất cả sinh viên
- ✅ GET `/api/students/{id}` - Lấy sinh viên theo ID
- ✅ POST `/api/students` - Thêm sinh viên mới
- ✅ PUT `/api/students/{id}` - Cập nhật sinh viên
- ✅ DELETE `/api/students/{id}` - Xóa sinh viên
- ✅ GET `/api/students/search?keyword=...` - Tìm kiếm

## 🚀 CÁCH SỬ DỤNG

### 1. Khởi động ứng dụng
```bash
# Chạy Spring Boot application
mvn spring-boot:run
# hoặc chạy từ IDE
```

### 2. Truy cập Website
- **Trang chủ:** `http://localhost:8080/students`
- **Thêm mới:** `http://localhost:8080/students/add`
- **Sửa:** `http://localhost:8080/students/edit/{id}`

### 3. Sử dụng REST API

#### Lấy tất cả sinh viên
```bash
GET http://localhost:8080/api/students
```

#### Lấy sinh viên theo ID
```bash
GET http://localhost:8080/api/students/1
```

#### Thêm sinh viên mới
```bash
POST http://localhost:8080/api/students
Content-Type: application/json

{
  "id": 1,
  "name": "Nguyễn Văn A",
  "email": "a@gmail.com",
  "sdt": "0912345678"
}
```

#### Cập nhật sinh viên
```bash
PUT http://localhost:8080/api/students/1
Content-Type: application/json

{
  "name": "Nguyễn Văn B",
  "email": "b@gmail.com",
  "sdt": "0923456789"
}
```

#### Xóa sinh viên
```bash
DELETE http://localhost:8080/api/students/1
```

#### Tìm kiếm
```bash
GET http://localhost:8080/api/students/search?keyword=Nguyễn
```

## 📋 CẤU TRÚC PROJECT

```
studentmanager/
├── src/main/java/com/example/studentmanager/
│   ├── controller/
│   │   ├── StudentController.java      # Web Controller (Thymeleaf)
│   │   └── StudentApiController.java   # REST API Controller
│   ├── service/
│   │   └── StudentService.java        # Business Logic
│   ├── repository/
│   │   └── StudentRepository.java     # Data Access
│   └── model/
│       └── Student.java               # Entity
└── src/main/resources/
    ├── templates/
    │   ├── students.html              # Trang danh sách
    │   └── student-form.html          # Form thêm/sửa
    └── application.properties         # Cấu hình
```

## 🎨 GIAO DIỆN

- ✅ Bootstrap 5.3.0
- ✅ Bootstrap Icons
- ✅ Responsive design
- ✅ Form validation
- ✅ Alert messages
- ✅ Search functionality

## 🔧 CẤU HÌNH DATABASE

Đảm bảo database `school1234` và bảng `sinhvien` đã được tạo:

```sql
USE school1234;
GO

CREATE TABLE sinhvien (
    id INT PRIMARY KEY,
    name NVARCHAR(100),
    email NVARCHAR(100),
    sdt NVARCHAR(20)
);
```

## 📝 LƯU Ý

1. **ID phải là số nguyên** - Không tự động tăng, cần nhập thủ công
2. **Email và Name là bắt buộc** - Form sẽ validate
3. **Số điện thoại là tùy chọn**
4. **Xóa có confirm** - Sẽ hỏi xác nhận trước khi xóa

## 🧪 TEST API VỚI POSTMAN/CURL

### Test với cURL:

```bash
# Lấy tất cả
curl http://localhost:8080/api/students

# Thêm mới
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"id":1,"name":"Test","email":"test@test.com","sdt":"123"}'

# Cập nhật
curl -X PUT http://localhost:8080/api/students/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated","email":"updated@test.com","sdt":"456"}'

# Xóa
curl -X DELETE http://localhost:8080/api/students/1

# Tìm kiếm
curl http://localhost:8080/api/students/search?keyword=Test
```

## ✅ HOÀN THÀNH

Tất cả tính năng CRUD đã được triển khai đầy đủ!
