-- =============================================
-- SCRIPT KIỂM TRA VÀ CẤP QUYỀN CHO USER SA
-- =============================================

-- Bước 1: Kiểm tra user sa có tồn tại không
SELECT name, type_desc, is_disabled 
FROM sys.server_principals 
WHERE name = 'sa';
GO

-- Nếu không có kết quả, user sa chưa được tạo hoặc đã bị xóa

-- =============================================
-- Bước 2: KIỂM TRA QUYỀN TRUY CẬP DATABASE
-- =============================================

USE school1234;
GO

-- Kiểm tra các user có quyền truy cập database school1234
SELECT 
    dp.name AS UserName,
    dp.type_desc AS UserType,
    ISNULL(USER_NAME(drm.role_principal_id), 'public') AS DatabaseRole
FROM sys.database_principals dp
LEFT JOIN sys.database_role_members drm ON dp.principal_id = drm.member_principal_id
WHERE dp.type IN ('S', 'U', 'G')
ORDER BY dp.name;
GO

-- Kiểm tra cụ thể user sa
SELECT 
    dp.name AS UserName,
    dp.type_desc AS UserType,
    ISNULL(USER_NAME(drm.role_principal_id), 'public') AS DatabaseRole
FROM sys.database_principals dp
LEFT JOIN sys.database_role_members drm ON dp.principal_id = drm.member_principal_id
WHERE dp.name = 'sa' OR dp.name LIKE '%sa%';
GO

-- =============================================
-- Bước 3: CẤP QUYỀN CHO USER SA (NẾU CHƯA CÓ)
-- =============================================

-- Nếu user sa chưa có quyền, chạy các lệnh sau:

USE master;
GO

-- Đảm bảo SQL Server Authentication được bật
-- (Phải làm thủ công trong SSMS: Server Properties > Security)

-- Đảm bảo user sa được enable và có password
-- ALTER LOGIN sa ENABLE;
-- ALTER LOGIN sa WITH PASSWORD = '123456'; -- Đổi password nếu cần
GO

-- Cấp quyền truy cập database school1234 cho user sa
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

-- Cấp quyền db_datareader (đọc dữ liệu)
ALTER ROLE db_datareader ADD MEMBER [sa];
GO

-- Cấp quyền db_datawriter (ghi dữ liệu)
ALTER ROLE db_datawriter ADD MEMBER [sa];
GO

-- Cấp quyền db_owner (toàn quyền - tùy chọn, chỉ dùng khi cần)
-- ALTER ROLE db_owner ADD MEMBER [sa];
-- GO

PRINT 'Đã cấp quyền db_datareader và db_datawriter cho user sa';
GO

-- =============================================
-- Bước 4: KIỂM TRA LẠI QUYỀN SAU KHI CẤP
-- =============================================

-- Kiểm tra quyền của user sa trong database school1234
SELECT 
    dp.name AS UserName,
    USER_NAME(drm.role_principal_id) AS DatabaseRole
FROM sys.database_principals dp
INNER JOIN sys.database_role_members drm ON dp.principal_id = drm.member_principal_id
WHERE dp.name = 'sa';
GO

-- Kiểm tra quyền chi tiết hơn
SELECT 
    p.name AS PrincipalName,
    p.type_desc AS PrincipalType,
    prm.permission_name AS Permission,
    prm.state_desc AS PermissionState,
    o.name AS ObjectName
FROM sys.database_permissions prm
INNER JOIN sys.database_principals p ON prm.grantee_principal_id = p.principal_id
LEFT JOIN sys.objects o ON prm.major_id = o.object_id
WHERE p.name = 'sa';
GO

PRINT '========================================';
PRINT 'HOÀN TẤT!';
PRINT '========================================';

