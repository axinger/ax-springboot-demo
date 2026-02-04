@echo off
setlocal

:: 设置应用程序名称（用于 jpackage 生成的目录名、安装包名等）
set APP_NAME=MyApp

:: 设置要打包的 JAR 文件名（必须与 Maven 构建生成的 JAR 文件名一致）
set JAR_FILE=a11-spring-boot-swagger3-*.jar

:: 设置输出目录（存放生成的应用镜像和安装程序）
set OUTPUT_DIR=output

:: ===== 步骤 1：使用 Maven 构建 JAR 文件 =====
echo Step 1: Building JAR file...
call mvn clean package
if errorlevel 1 (
    echo Error building JAR file
    pause
    exit /b 1
)

:: ===== 步骤 2：使用 jlink 创建精简的自定义 JRE =====
:: 检查是否已存在 custom-jre 目录，避免重复构建（节省时间）
echo Step 2: Creating custom JRE...
if not exist "custom-jre" (
    :: 使用 jlink 从 JAVA_HOME 的 jmods 目录中链接所需模块
    :: 注意：Spring Boot 应用通常需要较多模块，此处列出常见依赖
    jlink ^
        --module-path "%JAVA_HOME%\jmods" ^
        --add-modules java.base,java.desktop,java.sql,java.management,java.naming,java.net.http,jdk.httpserver ^
        --output ./custom-jre ^
        --strip-debug ^          :: 移除调试信息以减小体积
        --no-header-files ^      :: 不包含本地头文件（JNI 不需要可省略）
        --no-man-pages           :: 不包含手册页
    if errorlevel 1 (
        echo Error creating custom JRE
        pause
        exit /b 1
    )
)

:: ===== 步骤 3：使用 jpackage 创建独立应用镜像（app-image）=====
echo Step 3: Creating app image...
if not exist "%OUTPUT_DIR%" mkdir "%OUTPUT_DIR%"

:: 调用 jpackage 生成可执行应用镜像
jpackage ^
    --name "%APP_NAME%" ^                    :: 应用名称
    --input target ^                         :: 输入目录（Maven 默认将 JAR 放在 target/）
    --main-jar %JAR_FILE% ^                  :: 主 JAR 文件名
    --main-class org.springframework.boot.loader.JarLauncher ^  :: Spring Boot 可执行 JAR 的启动类
    --type app-image ^                       :: 生成类型为应用镜像（包含 exe、runtime 等）
    --runtime-image ./custom-jre ^           :: 使用上一步创建的自定义 JRE
    --win-console false  ^                   :: 是否显示控制台
    --dest "%OUTPUT_DIR%"                    :: 输出目录
if errorlevel 1 (
    echo Error creating app image
    pause
    exit /b 1
)

:: ===== 步骤 4：基于应用镜像生成 Windows 安装程序（MSI）=====
echo Step 4: Creating installer...
if not exist "%OUTPUT_DIR%/installer" mkdir "%OUTPUT_DIR%/installer"

jpackage ^
    --type msi ^                             :: 生成 MSI 安装包（Windows Installer）
    --app-image "%OUTPUT_DIR%/%APP_NAME%" ^  :: 指定上一步生成的应用镜像路径
    --name "%APP_NAME%Installer" ^           :: 安装程序显示名称
    --dest "%OUTPUT_DIR%/installer"          :: 安装包输出目录
if errorlevel 1 (
    echo Error creating installer
    pause
    exit /b 1
)

:: ===== 成功完成 =====
echo Packaging completed successfully!
echo App image: %OUTPUT_DIR%\%APP_NAME%
echo Installer: %OUTPUT_DIR%\installer\%APP_NAME%Installer.msi

pause