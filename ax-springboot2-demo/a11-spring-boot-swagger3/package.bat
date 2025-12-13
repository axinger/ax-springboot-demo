@echo off
setlocal

set APP_NAME=MyApp
set JAR_FILE=a11-spring-boot-swagger3-2025.01.01-2.7.jar
set OUTPUT_DIR=output

echo Step 1: Building JAR file...
call mvn clean package
if errorlevel 1 (
    echo Error building JAR file
    pause
    exit /b 1
)

echo Step 2: Creating custom JRE...
if not exist "custom-jre" (
    jlink --module-path %JAVA_HOME%\jmods --add-modules java.base,java.desktop,java.sql,java.management,java.naming,java.net.http,jdk.httpserver --output ./custom-jre --strip-debug --no-header-files --no-man-pages
    if errorlevel 1 (
        echo Error creating custom JRE
        pause
        exit /b 1
    )
)

echo Step 3: Creating app image...
if not exist "%OUTPUT_DIR%" mkdir "%OUTPUT_DIR%"
jpackage --name "%APP_NAME%" --input target --main-jar %JAR_FILE% --main-class org.springframework.boot.loader.JarLauncher --type app-image --runtime-image ./custom-jre --dest "%OUTPUT_DIR%"
if errorlevel 1 (
    echo Error creating app image
    pause
    exit /b 1
)

echo Step 4: Creating installer...
if not exist "%OUTPUT_DIR%/installer" mkdir "%OUTPUT_DIR%/installer"
jpackage --type msi --app-image "%OUTPUT_DIR%/%APP_NAME%" --name "%APP_NAME%Installer" --dest "%OUTPUT_DIR%/installer"
if errorlevel 1 (
    echo Error creating installer
    pause
    exit /b 1
)

echo Packaging completed successfully!
echo App image: %OUTPUT_DIR%\%APP_NAME%
echo Installer: %OUTPUT_DIR%\installer\%APP_NAME%Installer.msi

pause
