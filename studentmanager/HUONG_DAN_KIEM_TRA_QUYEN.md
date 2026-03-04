# 🔐 HƯỚNG DẪN KIỂM TRA VÀ CẤP QUYỀN CHO USER SA

## 📍 CÁCH KIỂM TRA QUYỀN TRONG SQL SERVER MANAGEMENT STUDIO (SSMS)

### **Cách 1: Kiểm tra qua Object Explorer (Dễ nhất)**

1. **Mở SSMS** và kết nối với SQL Server
2. **Mở rộng** cây bên trái:
   ```
   Server Name
   └── Security
       └── Logins
           └── sa (click phải → Properties)
   ```
3. **Kiểm tra:**
   - Tab **General**: Xem password và trạng thái (Enabled/Disabled)
   - Tab **Server Roles**: Xem các quyền server-level
   - Tab **User Mapping**: Xem các database mà user có quyền truy cập
     - Tìm database `school1234`
     - Kiểm tra các role: `db_datareader`, `db_datawriter`, `db_owner`

### **Cách 2: Kiểm tra qua Query (Chi tiết hơn)**

**Chạy các query sau trong SSMS:**

#### **1. Kiểm tra user sa có tồn tại và được enable không:**
```sql
SELECT name, type_desc, is_disabled, create_date
FROM sys.server_principals 
WHERE name = 'sa';
```

**Kết quả mong đợi:**
- `name = 'sa'`
- `type_desc = 'SQL_LOGIN'`
- `is_disabled = 0` (0 = enabled, 1 = disabled)

#### **2. Kiểm tra user sa có quyền truy cập database school1234:**
```sql
USE school1234;
GO

SELECT 
    dp.name AS UserName,
    dp.type_desc AS UserType,
    ISNULL(USER_NAME(drm.role_principal_id), 'public') AS DatabaseRole
FROM sys.database_principals dp
LEFT JOIN sys.database_role_members drm ON dp.principal_id = drm.member_principal_id
WHERE dp.name = 'sa';
```

**Kết quả mong đợi:**
- Có ít nhất 1 dòng với `UserName = 'sa'`
- `DatabaseRole` nên có: `db_datareader` hoặc `db_datawriter` hoặc `db_owner`

#### **3. Kiểm tra quyền chi tiết:**
```sql
USE school1234;
GO

SELECT 
    p.name AS PrincipalName,
    prm.permission_name AS Permission,
    prm.state_desc AS PermissionState
FROM sys.database_permissions prm
INNER JOIN sys.database_principals p ON prm.grantee_principal_id = p.principal_id
WHERE p.name = 'sa';
```

---

## ✅ CẤP QUYỀN CHO USER SA (NẾU CHƯA CÓ)

### **Bước 1: Enable SQL Server Authentication**

1. **Click phải vào Server** (ở Object Explorer) → **Properties**
2. Tab **Security**
3. Chọn **SQL Server and Windows Authentication mode**
4. Click **OK**
5. **Restart SQL Server** (click phải Server → Restart)

### **Bước 2: Enable và đặt password cho user sa**

```sql
-- Enable user sa
ALTER LOGIN sa ENABLE;
GO

-- Đặt password cho user sa (nếu chưa có hoặc muốn đổi)
ALTER LOGIN sa WITH PASSWORD = '123456';
GO
```

### **Bước 3: Cấp quyền truy cập database school1234**

```sql
USE school1234;
GO

-- Tạo user trong database (nếu chưa có)
IF NOT EXISTS (SELECT * FROM sys.database_principals WHERE name = 'sa')
BEGIN
    CREATE USER [sa] FOR LOGIN [sa];
END
GO

-- Cấp quyền đọc dữ liệu
ALTER ROLE db_datareader ADD MEMBER [sa];
GO

-- Cấp quyền ghi dữ liệu
ALTER ROLE db_datawriter ADD MEMBER [sa];
GO

-- (Tùy chọn) Cấp toàn quyền
-- ALTER ROLE db_owner ADD MEMBER [sa];
-- GO
```

### **Bước 4: Test kết nối**

1. **Disconnect** khỏi SSMS hiện tại
2. **Connect lại** với:
   - **Server name**: `localhost\SQLEXPRESS` hoặc `.\SQLEXPRESS`
   - **Authentication**: **SQL Server Authentication**
   - **Login**: `sa`
   - **Password**: `123456`
3. Nếu kết nối được → User sa đã có quyền!

---

## 🔍 CÁC VẤN ĐỀ THƯỜNG GẶP

### **Vấn đề 1: User sa bị disabled**
**Giải pháp:**
```sql
ALTER LOGIN sa ENABLE;
```

### **Vấn đề 2: SQL Server Authentication chưa được bật**
**Giải pháp:**
- Server Properties → Security → Chọn "SQL Server and Windows Authentication mode"
- Restart SQL Server

### **Vấn đề 3: User sa không có quyền truy cập database**
**Giải pháp:**
- Chạy script cấp quyền ở trên (Bước 3)

### **Vấn đề 4: Password không đúng**
**Giải pháp:**
```sql
ALTER LOGIN sa WITH PASSWORD = 'your_new_password';
```
Sau đó cập nhật password trong `application.properties`

---

## 📝 SỬ DỤNG SCRIPT TỰ ĐỘNG

**Chạy file `check_and_grant_permissions.sql`** trong SSMS:
1. Mở file trong SSMS
2. Chạy toàn bộ script
3. Xem kết quả trong Messages tab

---

## ✅ KIỂM TRA NHANH

**Test kết nối từ ứng dụng Spring Boot:**
1. Chạy ứng dụng
2. Truy cập: `http://localhost:8080/test/db-connection`
3. Xem kết quả JSON:
   - `"connected": true` → Thành công!
   - `"connected": false` → Kiểm tra lại các bước trên

---

## 🎯 TÓM TẮT

**Để user sa có quyền truy cập database school1234:**

1. ✅ Enable SQL Server Authentication
2. ✅ Enable user sa
3. ✅ Đặt password cho user sa
4. ✅ Tạo user trong database school1234
5. ✅ Cấp quyền db_datareader và db_datawriter
6. ✅ Test kết nối

**Xem chi tiết trong file:** `check_and_grant_permissions.sql`

