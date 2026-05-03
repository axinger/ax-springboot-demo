#!/bin/bash
# Arthas Demo 快速启动脚本 (Linux/Mac)

echo "========================================"
echo "  Arthas Demo 应用启动脚本"
echo "========================================"
echo ""

echo "[1] 清理之前的构建..."
mvn clean

echo ""
echo "[2] 编译项目..."
mvn package -DskipTests

if [ $? -ne 0 ]; then
    echo ""
    echo "错误: 编译失败!"
    exit 1
fi

echo ""
echo "[3] 启动应用..."
echo ""
echo "========================================"
echo "  应用正在启动..."
echo "  Arthas Web Console: http://127.0.0.1:8563"
echo "  API 地址: http://127.0.0.1:15024"
echo "  按 Ctrl+C 停止应用"
echo "========================================"
echo ""

mvn spring-boot:run
