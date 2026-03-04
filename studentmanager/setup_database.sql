-- =============================================
-- SCRIPT TẠO DATABASE VÀ BẢNG CHO LAB 03
-- =============================================

-- Bước 1: Tạo database (nếu chưa có)
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'school')
BEGIN
    CREATE DATABASE school;
    PRINT 'Database "school" đã được tạo thành công!';
END
ELSE
BEGIN
    PRINT 'Database "school" đã tồn tại.';
END
GO

-- Bước 2: Sử dụng database school
USE school;
GO

-- Bước 3: Tạo bảng students (nếu chưa có)
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'students')
BEGIN
    CREATE TABLE students (
        id INT PRIMARY KEY,
        name NVARCHAR(100),
        age INT,
        email NVARCHAR(100)
    );
    PRINT 'Bảng "students" đã được tạo thành công!';
END
ELSE
BEGIN
    PRINT 'Bảng "students" đã tồn tại.';
    -- Xóa dữ liệu cũ nếu muốn (tùy chọn)
    -- DELETE FROM students;
END
GO

-- Bước 4: Thêm dữ liệu mẫu
IF NOT EXISTS (SELECT * FROM students WHERE id = 1)
BEGIN
    INSERT INTO students VALUES
    (1, N'Nguyễn Văn A', 20, 'a@gmail.com'),
    (2, N'Trần Thị B', 21, 'b@gmail.com'),
    (3, N'Lê Văn C', 19, 'c@gmail.com');
    PRINT 'Đã thêm 3 sinh viên mẫu vào bảng!';
END
ELSE
BEGIN
    PRINT 'Dữ liệu mẫu đã tồn tại.';
END
GO

-- Bước 5: Kiểm tra dữ liệu
SELECT * FROM students;
GO

PRINT '========================================';
PRINT 'HOÀN TẤT! Database đã sẵn sàng.';
PRINT '========================================';

