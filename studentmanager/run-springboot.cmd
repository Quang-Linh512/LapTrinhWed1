@echo off
chcp 65001 >nul
set JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8
cd /d "%~dp0"

REM Build JAR (tránh lỗi classpath khi đường dẫn có dấu)
call "%~dp0mvnw.cmd" clean package -DskipTests -q
if errorlevel 1 exit /b 1

REM Chạy bằng java -jar (class nằm trong JAR, không phụ thuộc đường dẫn)
java -jar target\studentmanager-0.0.1-SNAPSHOT.jar %*
