@echo off
REM Arthas Demo 快速启动脚本 (Windows)

echo ========================================
echo   Arthas Demo 应用启动脚本
echo ========================================
echo.

echo [1] 清理之前的构建...
call mvn clean

echo.
echo [2] 编译项目...
call mvn package -DskipTests

if %errorlevel% neq 0 (
    echo.
    echo 错误: 编译失败!
    pause
    exit /b 1
)

echo.
echo [3] 启动应用...
echo.
echo ========================================
echo   应用正在启动...
echo   Arthas Web Console: http://127.0.0.1:8563
echo   API 地址: http://127.0.0.1:15024
echo   按 Ctrl+C 停止应用
echo ========================================
echo.

call mvn spring-boot:run

pause
