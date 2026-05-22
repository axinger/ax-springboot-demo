#!/bin/bash
# ==========================================
# Jenkins 预构建步骤：批量安装依赖模块
# 功能: 编译并安装项目依赖的公共模块
# 时区配置: Asia/Shanghai (北京)
# ==========================================

# --- 配置区 ---
# 定义依赖模块列表（根据实际项目结构调整）
declare -a DEPENDENCY_MODULES=(
    "ax-enterprise-common/common-core"
    # 添加更多依赖模块...
)

# 强制设置时区为北京时间 (CST)，确保日志时间准确
export TZ='Asia/Shanghai'

# Maven 构建参数
MAVEN_OPTS="-Xms512m -Xmx2048m"
MAVEN_COMMAND="mvn clean install -B -DskipTests"

# --- 函数区 ---
log_with_time() {
    # 统一日志格式，包含精确到毫秒的时间戳
    echo "[$(date '+%Y-%m-%d %H:%M:%S.%3N %Z')] $1"
}

check_maven() {
    # 检查 Maven 是否可用
    if ! command -v mvn &> /dev/null; then
        log_with_time "❌ 错误：Maven 未安装或未添加到 PATH"
        exit 1
    fi
    log_with_time "✅ Maven 版本: $(mvn -version | head -n 1)"
}

# --- 主逻辑 ---
log_with_time "========================================="
log_with_time "🚀 开始批量构建依赖模块 (北京时区: $TZ)"
log_with_time "包含模块: ${DEPENDENCY_MODULES[@]}"
log_with_time "Maven 命令: $MAVEN_COMMAND"
log_with_time "========================================="

# 检查 Maven
check_maven

# 检查工作空间
if [ -z "$WORKSPACE" ]; then
    log_with_time "⚠️ 警告：Jenkins WORKSPACE 未定义，使用当前目录"
    WORKSPACE=$(pwd)
fi

log_with_time "📂 工作空间: $WORKSPACE"

# 遍历模块
SUCCESS_COUNT=0
FAIL_COUNT=0

for module_rel_path in "${DEPENDENCY_MODULES[@]}"; do
    FULL_PATH="${WORKSPACE}/${module_rel_path}"
    log_with_time ""
    log_with_time "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
    log_with_time "📦 处理模块: $module_rel_path"
    log_with_time "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

    # 1. 路径检查
    if [ ! -d "$FULL_PATH" ]; then
        log_with_time "❌ 错误：目录不存在 -> $FULL_PATH"
        log_with_time "💡 提示：请确认代码是否已正确检出，或调整模块路径"
        FAIL_COUNT=$((FAIL_COUNT + 1))
        continue
    fi

    # 2. 文件检查
    if [ ! -f "$FULL_PATH/pom.xml" ]; then
        log_with_time "❌ 错误：未找到 pom.xml 文件 -> $FULL_PATH/pom.xml"
        FAIL_COUNT=$((FAIL_COUNT + 1))
        continue
    fi

    # 3. 执行构建
    log_with_time "⏳ 正在构建: $module_rel_path ..."
    START_TIME=$(date +%s)

    # 进入目录并执行 Maven
    cd "$FULL_PATH" || {
        log_with_time "❌ 错误：无法进入目录 $FULL_PATH"
        FAIL_COUNT=$((FAIL_COUNT + 1))
        continue
    }

    # Maven 命令执行
    if eval "$MAVEN_COMMAND"; then
        END_TIME=$(date +%s)
        DURATION=$((END_TIME - START_TIME))
        log_with_time "✅ 成功: $module_rel_path 构建完成 (耗时: ${DURATION}秒)"
        SUCCESS_COUNT=$((SUCCESS_COUNT + 1))
    else
        log_with_time "❌ 失败: $module_rel_path 构建异常"
        log_with_time "💡 提示：查看上方 Maven 输出日志定位问题"
        FAIL_COUNT=$((FAIL_COUNT + 1))
        
        # 如果设置了严格模式，遇到失败立即退出
        if [ "${STRICT_MODE:-false}" = "true" ]; then
            log_with_time "🛑 严格模式：遇到失败立即退出"
            exit 1
        fi
    fi
done

# --- 总结 ---
log_with_time ""
log_with_time "========================================="
log_with_time "📊 构建结果统计"
log_with_time "========================================="
log_with_time "总模块数: ${#DEPENDENCY_MODULES[@]}"
log_with_time "成功: $SUCCESS_COUNT"
log_with_time "失败: $FAIL_COUNT"
log_with_time "当前服务器时间: $(date '+%Y-%m-%d %H:%M:%S %Z')"
log_with_time "========================================="

if [ $FAIL_COUNT -gt 0 ]; then
    log_with_time "❌ 部分模块构建失败，请检查日志"
    exit 1
else
    log_with_time "🎉 所有预构建步骤成功完成！"
    exit 0
fi
