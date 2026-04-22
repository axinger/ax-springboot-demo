@echo off

:: ===========================================
:: Axinger 模块一键编译脚本 (Windows)
:: ===========================================

echo 🚀 开始编译所有 axinger 模块...
echo 工作目录: %CD%
echo.

:: 记录开始时间
set START_TIME=%TIME%

echo 📦 正在编译 27 个模块...
echo.

:: 执行 Maven 编译
mvn clean install -DskipTests

:: 检查编译结果
if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✅ 编译成功！

    echo 📊 编译统计:
    echo   总模块数: 27 个
    echo   本地仓库: %%USERPROFILE%%\.m2\repository\com\github\axinger\
    echo.

    :: 统计生成的文件
    for /f "tokens=*" %%i in ('dir /s /b "%USERPROFILE%\.m2\repository\com\github\axinger\*.jar" ^| find /c /v ""') do set JAR_COUNT=%%i
    for /f "tokens=*" %%i in ('dir /s /b "%USERPROFILE%\.m2\repository\com\github\axinger\*-sources.jar" ^| find /c /v ""') do set SOURCE_COUNT=%%i

    echo 📦 生成文件统计:
    echo   JAR 文件: %JAR_COUNT% 个
    echo   源码文件: %SOURCE_COUNT% 个
    echo.

    echo 🎉 所有 axinger 模块已成功安装到本地 Maven 仓库！

) else (
    echo.
    echo ❌ 编译失败！
    echo 请检查错误信息并解决后重试。
    exit /b 1
)