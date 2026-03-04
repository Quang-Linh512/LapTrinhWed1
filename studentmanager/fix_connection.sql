-- =============================================
-- SCRIPT SỬA LỖI KẾT NỐI - CHẠY TRONG SSMS
-- =============================================

-- Bước 1: Enable SQL Server Authentication
-- (Phải làm thủ công trong SSMS: Server Properties > Security > SQL Server and Windows Authentication mode)
-- Sau đó RESTART SQL Server

-- Bước 2: Enable và đặt password cho user sa
USE master;
GO

-- Enable user sa
ALTER LOGIN sa ENABLE;
GO

-- Đặt password (nếu chưa có hoặc muốn đổi)
ALTER LOGIN sa WITH PASSWORD = '123456';
GO

PRINT 'Đã enable user sa và đặt password = 123456';
GO

-- Bước 3: Cấp quyền truy cập database school1234
USE school1234;
GO

-- Tạo user trong database (nếu chưa có)
IF NOT EXISTS (SELECT * FROM sys.database_principals WHERE name = 'sa')
BEGIN
    CREATE USER [sa] FOR LOGIN [sa];
    PRINT 'Đã tạo user sa trong database school1234';
END
ELSE
BEGIN
    PRINT 'User sa đã tồn tại trong database school1234';
END
GO

-- Cấp quyền đọc dữ liệu
IF NOT EXISTS (
    SELECT * FROM sys.database_role_members 
    WHERE member_principal_id = USER_ID('sa') 
    AND role_principal_id = DATABASE_PRINCIPAL_ID('db_datareader')
)
BEGIN
    ALTER ROLE db_datareader ADD MEMBER [sa];
    PRINT 'Đã cấp quyền db_datareader cho user sa';
END
ELSE
BEGIN
    PRINT 'User sa đã có quyền db_datareader';
END
GO

-- Cấp quyền ghi dữ liệu
IF NOT EXISTS (
    SELECT * FROM sys.database_role_members 
    WHERE member_principal_id = USER_ID('sa') 
    AND role_principal_id = DATABASE_PRINCIPAL_ID('db_datawriter')
)
BEGIN
    ALTER ROLE db_datawriter ADD MEMBER [sa];
    PRINT 'Đã cấp quyền db_datawriter cho user sa';
END
ELSE
BEGIN
    PRINT 'User sa đã có quyền db_datawriter';
END
GO

-- Bước 4: Kiểm tra lại quyền
SELECT 
    dp.name AS UserName,
    USER_NAME(drm.role_principal_id) AS DatabaseRole
FROM sys.database_principals dp
INNER JOIN sys.database_role_members drm ON dp.principal_id = drm.member_principal_id
WHERE dp.name = 'sa';
GO

PRINT '========================================';
PRINT 'HOÀN TẤT!';
PRINT '========================================';
PRINT 'Bây giờ hãy:';
PRINT '1. Test kết nối trong SSMS với SQL Server Authentication';
PRINT '   - Server: localhost\SQLEXPRESS';
PRINT '   - Login: sa';
PRINT '   - Password: 123456';
PRINT '2. Restart Spring Boot application';
PRINT '3. Truy cập: http://localhost:8080/students';
PRINT '========================================';

