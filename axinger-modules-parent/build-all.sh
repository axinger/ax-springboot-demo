#!/bin/bash

# ===========================================
# Axinger 模块一键编译脚本
# ===========================================

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}🚀 开始编译所有 axinger 模块...${NC}"
echo -e "${YELLOW}工作目录: $(pwd)${NC}"
echo ""

# 记录开始时间
START_TIME=$(date +%s)

# 编译所有模块
echo -e "${GREEN}📦 正在编译 27 个模块...${NC}"
echo ""

# 执行 Maven 编译
mvn clean install -DskipTests

# 检查编译结果
if [ $? -eq 0 ]; then
    echo ""
    echo -e "${GREEN}✅ 编译成功！${NC}"

    # 计算耗时
    END_TIME=$(date +%s)
    DURATION=$((END_TIME - START_TIME))

    echo -e "${GREEN}📊 编译统计:${NC}"
    echo -e "  总模块数: 27 个"
    echo -e "  耗时: ${DURATION} 秒"
    echo -e "  本地仓库: ~/.m2/repository/com/github/axinger/"
    echo ""

    # 统计生成的文件
    JAR_COUNT=$(find ~/.m2/repository/com/github/axinger -name "*.jar" | wc -l)
    SOURCE_COUNT=$(find ~/.m2/repository/com/github/axinger -name "*-sources.jar" | wc -l)

    echo -e "${GREEN}📦 生成文件统计:${NC}"
    echo -e "  JAR 文件: ${JAR_COUNT} 个"
    echo -e "  源码文件: ${SOURCE_COUNT} 个"
    echo ""

    echo -e "${GREEN}🎉 所有 axinger 模块已成功安装到本地 Maven 仓库！${NC}"

else
    echo ""
    echo -e "${RED}❌ 编译失败！${NC}"
    echo -e "${YELLOW}请检查错误信息并解决后重试。${NC}"
    exit 1
fi