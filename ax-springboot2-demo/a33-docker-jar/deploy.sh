#!/bin/bash
# ===========================================================================
# 企业级部署脚本
# 用法: ./deploy.sh [方案名称] [环境]
# 示例: ./deploy.sh external-config production
# ===========================================================================

set -e

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo_with_color() {
    echo -e "${1}${2}${NC}"
}

# 默认参数
SCHEME="external-config"
ENVIRONMENT="production"

# 解析命令行参数
while [[ $# -gt 0 ]]; do
    case $1 in
        -s|--scheme)
            SCHEME="$2"
            shift 2
            ;;
        -e|--environment)
            ENVIRONMENT="$2"
            shift 2
            ;;
        -h|--help)
            echo "Usage: $0 [OPTIONS]"
            echo "企业级Spring Boot应用部署脚本"
            echo ""
            echo "Options:"
            echo "  -s, --scheme SCHEME    部署方案: external-config, env-vars, config-center"
            echo "  -e, --environment ENV  部署环境: production, staging, development"
            echo "  -h, --help            显示此帮助信息"
            echo ""
            echo "可用方案:"
            echo "  external-config  - 配置外部化方案（推荐）"
            echo "  env-vars        - 环境变量方案"
            echo "  config-center   - 配置中心方案"
            exit 0
            ;;
        *)
            echo "未知参数: $1"
            exit 1
            ;;
    esac
done

echo_with_color ${GREEN} "开始部署: 方案=${SCHEME}, 环境=${ENVIRONMENT}"

# 验证方案是否存在
case $SCHEME in
    "external-config")
        DOCKERFILE="Dockerfile.external-config"
        COMPOSE_FILE="docker-compose.external-config.yml"
        ;;
    "env-vars")
        DOCKERFILE="Dockerfile.env-vars"
        COMPOSE_FILE="docker-compose.env-vars.yml"
        ;;
    "config-center")
        DOCKERFILE="Dockerfile.config-center"
        COMPOSE_FILE="docker-compose.config-center.yml"
        ;;
    *)
        echo_with_color ${RED} "错误: 不支持的部署方案 '$SCHEME'"
        exit 1
        ;;
esac

# 检查文件是否存在
if [ ! -f "$DOCKERFILE" ]; then
    echo_with_color ${RED} "错误: Dockerfile '$DOCKERFILE' 不存在"
    exit 1
fi

if [ ! -f "$COMPOSE_FILE" ]; then
    echo_with_color ${RED} "错误: Compose文件 '$COMPOSE_FILE' 不存在"
    exit 1
fi

echo_with_color ${YELLOW} "步骤1: 清理旧版本..."
docker-compose -f $COMPOSE_FILE down --remove-orphans

echo_with_color ${YELLOW} "步骤2: 构建应用..."
mvn clean package -DskipTests -Pprd

if [ $? -ne 0 ]; then
    echo_with_color ${RED} "Maven构建失败"
    exit 1
fi

echo_with_color ${YELLOW} "步骤3: 构建Docker镜像..."
docker-compose -f $COMPOSE_FILE build

if [ $? -ne 0 ]; then
    echo_with_color ${RED} "Docker构建失败"
    exit 1
fi

echo_with_color ${YELLOW} "步骤4: 启动服务..."
docker-compose -f $COMPOSE_FILE up -d

echo_with_color ${YELLOW} "步骤5: 等待服务启动..."
sleep 30

echo_with_color ${YELLOW} "步骤6: 检查服务健康状态..."
if curl -f http://localhost:13301/axinger/actuator/health > /dev/null 2>&1; then
    echo_with_color ${GREEN} "✅ 服务启动成功!"
    echo "应用访问地址: http://localhost:13301/axinger"
    echo "健康检查: http://localhost:13301/axinger/actuator/health"
else
    echo_with_color ${RED} "❌ 服务启动失败，请检查日志"
    docker-compose -f $COMPOSE_FILE logs
    exit 1
fi

echo_with_color ${GREEN} "🎉 部署完成!"

# 显示服务信息
echo ""
echo "=== 服务信息 ==="
docker-compose -f $COMPOSE_FILE ps

echo ""
echo "=== 查看日志 ==="
echo "docker-compose -f $COMPOSE_FILE logs -f"