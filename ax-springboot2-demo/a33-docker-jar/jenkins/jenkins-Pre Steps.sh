#!/bin/bash
# ==========================================
# 预构建步骤：批量安装 依赖模块
# 支持模块: A, B
# 时区配置: Asia/Shanghai (北京)
# ==========================================

# --- 配置区 ---
# 定义依赖模块列表（你的输入）
declare -a DEPENDENCY_MODULES=(
    "src/a-common"
    "src/b-common"
)

# 强制设置时区为北京时间 (CST)，确保日志时间准确
export TZ='Asia/Shanghai'

# --- 函数区 ---
log_with_time() {
    # 统一日志格式，包含精确到毫秒的时间戳
    echo "[$(date '+%Y-%m-%d %H:%M:%S.%3N %Z')] $1"
}

# --- 主逻辑 ---
log_with_time "========================================="
log_with_man "🚀 开始批量构建依赖模块 (北京时区: $TZ)"
log_with_time "包含模块: ${DEPENDENCY_MODULES[@]}"
log_with_time "========================================="

# 检查工作空间
if [ ! -d "$WORKSPACE" ]; then
    log_with_time "❌ 错误：Jenkins WORKSPACE 未定义或不存在"
    exit 1
fi

# 遍历模块
for module_rel_path in "${DEPENDENCY_MODULES[@]}"; do
    FULL_PATH="${WORKSPACE}/${module_rel_path}"
    log_with_time "--- 处理模块: $module_rel_path ---"

    # 1. 路径检查
    if [ ! -d "$FULL_PATH" ]; then
        log_with_time "❌ 错误：目录不存在 -> $FULL_PATH"
        log_with_time "💡 提示：请确认代码是否已正确检出"
        exit 1
    fi

    # 2. 文件检查
    if [ ! -f "$FULL_PATH/pom.xml" ]; then
        log_with_time "❌ 错误：未找到 pom.xml 文件"
        exit 1
    fi

    # 3. 执行构建
    log_with_time "📦 正在构建: $module_rel_path ..."

    # 进入目录并执行 Maven
    cd "$FULL_PATH" || exit 1

    # Maven 命令优化: -B (批处理模式), -q (减少日志量，除非调试可去掉)
    mvn clean install -B -DskipTests -f pom.xml

    if [ $? -eq 0 ]; then
        log_with_time " 成功: $module_rel_path 构建完成"
    else
        log_with_time " 失败: $module_rel_path 构建异常"
        exit 1
    fi
done

log_with_time "========================================="
log_with_time "🎉 所有预构建步骤成功完成！"
log_with_time "当前服务器时间: $(date)"
log_with_time "========================================="
