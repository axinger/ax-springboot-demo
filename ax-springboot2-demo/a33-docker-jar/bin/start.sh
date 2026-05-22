#!/bin/bash
# ============================================================================
# 应用启动脚本 (Emoji 中文版 - 最终完善版)
# 支持: 命令行参数 > .env配置文件 > 脚本内默认配置 > 系统自动检测
# ============================================================================

source /etc/profile

# ------------------- 默认基础配置 (可直接在此处修改) -------------------
APP_PATH="/opt/application/demo-app"
APP_NAME="demo-application"
LOG_PATH="/opt/logs/demo"
NACOS_SERVER="localhost:8848"
MODE="cluster"
JAVA_VERSION=""  # 留空则自动检测系统默认Java，也可填写 8, 11, 17, 21
# ---------------------------------------------------------------------

# 获取当前脚本所在的绝对路径
BASE_DIR=$(cd "$(dirname "$0")" && pwd)
PID_FILE="${BASE_DIR}/${APP_NAME}.pid"

# 🔍 尝试加载同目录下的 .env 配置文件 (如果存在)
if [ -f "${BASE_DIR}/.env" ]; then
    echo "📄 发现配置文件 .env，正在加载自定义默认值..."
    source "${BASE_DIR}/.env"
fi

# ❌ 错误退出函数
error_exit() {
    echo "❌ 致命错误: $1 !!"
    exit 1
}

# 📝 解析命令行参数 (优先级最高，会覆盖上述所有默认配置)
while getopts ":p:n:a:l:m:j:" opt; do
    case $opt in
        m) MODE=$OPTARG;;           # 运行模式: standalone/cluster
        p) APP_PATH=$OPTARG;;       # 应用路径
        n) APP_NAME=$OPTARG;;       # 应用名称
        a) NACOS_SERVER=$OPTARG;;   # Nacos 服务器地址
        l) LOG_PATH=$OPTARG;;       # 日志路径
        j) JAVA_VERSION=$OPTARG;;   # Java 版本 (8, 11, 17, 21)
        ?) echo "⚠️ 未知参数"; exit 1;;
    esac
done

# 重新初始化 PID 文件和日志路径
PID_FILE="${BASE_DIR}/${APP_NAME}.pid"
LOG_FILE="${LOG_PATH}/${APP_NAME}.log"
GC_LOG_FILE="${LOG_PATH}/${APP_NAME}/${APP_NAME}_gc.log"

# ☕ 自动检测或指定 JAVA_HOME
if [ -n "$JAVA_VERSION" ]; then
    # 如果指定了 Java 版本 (通过 -j 参数或配置文件)，按版本查找对应的 JDK 路径
    echo "🔍 正在根据配置查找 Java ${JAVA_VERSION}..."

    # 常见的多版本 Java 安装路径
    case "$JAVA_VERSION" in
        8|1.8)
            JAVA_CANDIDATES=(
                "/usr/lib/jvm/java-8-openjdk-amd64"
                "/usr/lib/jvm/java-1.8.0-openjdk"
                "/usr/java/jdk1.8.0_latest"
                "/opt/jdk8"
                "$HOME/jdk8"
            )
            ;;
        11)
            JAVA_CANDIDATES=(
                "/usr/lib/jvm/java-11-openjdk-amd64"
                "/usr/lib/jvm/java-11-openjdk"
                "/usr/java/jdk-11"
                "/opt/jdk11"
                "$HOME/jdk11"
            )
            ;;
        17)
            JAVA_CANDIDATES=(
                "/usr/lib/jvm/java-17-openjdk-amd64"
                "/usr/lib/jvm/java-17-openjdk"
                "/usr/java/jdk-17"
                "/opt/jdk17"
                "$HOME/jdk17"
            )
            ;;
        21)
            JAVA_CANDIDATES=(
                "/usr/lib/jvm/java-21-openjdk-amd64"
                "/usr/lib/jvm/java-21-openjdk"
                "/usr/java/jdk-21"
                "/opt/jdk21"
                "$HOME/jdk21"
            )
            ;;
        *)
            error_exit "不支持的 Java 版本: ${JAVA_VERSION} (支持的版本: 8, 11, 17, 21)"
            ;;
    esac

    # 在候选路径中查找
    JAVA_HOME_FOUND=false
    for path in "${JAVA_CANDIDATES[@]}"; do
        if [ -e "$path/bin/java" ]; then
            JAVA_HOME="$path"
            JAVA_HOME_FOUND=true
            echo "✅ 成功定位到 Java ${JAVA_VERSION}: $JAVA_HOME"
            break
        fi
    done

    if [ "$JAVA_HOME_FOUND" = false ]; then
        error_exit "未在预设路径中找到 Java ${JAVA_VERSION}，请检查服务器安装情况或在脚本中补充路径。"
    fi
else
    # 未指定版本，使用系统默认的 JAVA_HOME 或自动检测
    if [ -z "$JAVA_HOME" ]; then
        echo "🔍 未指定特定 Java 版本，正在尝试自动检测系统默认环境..."

        # 常见的 Java 安装路径列表
        JAVA_HOME_CANDIDATES=(
            "$HOME/jdk/java"
            "/usr/java"
            "/usr/lib/jvm/default-java"
            "/Library/Java/JavaVirtualMachines"
        )

        for path in "${JAVA_HOME_CANDIDATES[@]}"; do
            if [ -e "$path/bin/java" ]; then
                JAVA_HOME="$path"
                break
            fi
        done

        # 兜底方案：尝试通过 javac 命令推断
        if [ -z "$JAVA_HOME" ] && command -v javac &> /dev/null; then
            JAVA_PATH=$(dirname "$(readlink -f "$(which javac)")")
            JAVA_HOME=$(dirname "$JAVA_PATH")
        fi

        if [ -z "$JAVA_HOME" ]; then
            error_exit "未能检测到可用的 Java 环境！请在环境变量中设置 JAVA_HOME，或使用 -j 参数指定版本。"
        else
            echo "✅ 自动检测到系统默认 Java 环境: $JAVA_HOME"
        fi
    fi
fi

JAVA="$JAVA_HOME/bin/java"

# 📋 显示 Java 版本信息
echo "☕ 当前使用的 Java 版本信息:"
$JAVA -version 2>&1 | head -n 1
echo ""

# 📦 检查 JAR 包是否存在
JAR_FILE="${APP_PATH}/${APP_NAME}.jar"
if [ ! -f "$JAR_FILE" ]; then
    error_exit "找不到目标 Jar 包: $JAR_FILE"
fi

# 🛑 检查是否已有进程在运行
if [ -f "$PID_FILE" ]; then
    OLD_PID=$(cat "$PID_FILE")
    if ps -p "$OLD_PID" > /dev/null 2>&1; then
        echo "⚠️ 应用 ${APP_NAME} 似乎已经在运行中 (PID: $OLD_PID)，请勿重复启动！"
        exit 1
    else
        echo "🧹 发现残留的 PID 文件，正在清理..."
        rm -f "$PID_FILE"
    fi
fi

# ⚙️ JVM 参数配置
JAVA_OPT=""
if [[ "${MODE}" == "standalone" ]]; then
    JAVA_OPT="${JAVA_OPT} -Xms512m -Xmx512m -Xmn256m"
else
    JAVA_OPT="${JAVA_OPT} -server -Xms512m -Xmx512m -Xmn512m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=160m"
    JAVA_OPT="${JAVA_OPT} -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${LOG_PATH}/${APP_NAME}/${APP_NAME}_heapdump.hprof"
    JAVA_OPT="${JAVA_OPT} -XX:-UseLargePages"
fi

# 📝 GC 日志配置 (兼容 Java 8 与 Java 9+)
JAVA_MAJOR_VERSION=$($JAVA -version 2>&1 | sed -E -n 's/.* version "([0-9]*).*$/\1/p')
if [[ "$JAVA_MAJOR_VERSION" -ge "9" ]] ; then
  JAVA_OPT="${JAVA_OPT} -Xlog:gc*:file=${GC_LOG_FILE}:time,tags:filecount=10,filesize=102400"
else
  JAVA_OPT="${JAVA_OPT} -Djava.ext.dirs=${JAVA_HOME}/jre/lib/ext:${JAVA_HOME}/lib/ext"
  JAVA_OPT="${JAVA_OPT} -Xloggc:${GC_LOG_FILE} -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=100M"
fi

# 🔗 应用启动参数
JAVA_OPT="${JAVA_OPT} -Dspring.cloud.nacos.config.server-addr=${NACOS_SERVER}"
JAVA_OPT="${JAVA_OPT} -Dspring.cloud.nacos.discovery.server-addr=${NACOS_SERVER}"
JAVA_OPT="${JAVA_OPT} --spring.config.location=classpath:/,classpath:/config/,file:./,file:./config/"
JAVA_OPT="${JAVA_OPT} --server.max-http-header-size=524288"
JAVA_OPT="${JAVA_OPT} ${JAVA_OPT_EXT}"

# 📂 创建必要的目录
mkdir -p "$(dirname "$LOG_FILE")"
mkdir -p "$(dirname "$GC_LOG_FILE")"

echo ""
echo "🚀 正在启动应用: ${APP_NAME} ..."
echo "☕ Java 路径: $JAVA_HOME"
echo "⚙️ 运行模式: $MODE"
echo "📄 日志文件: $LOG_FILE"
echo "🔗 Nacos 地址: $NACOS_SERVER"
echo "----------------------------------------"

# 启动应用，并将标准输出和错误输出重定向到日志文件
nohup $JAVA -jar "$JAR_FILE" $JAVA_OPT >> "$LOG_FILE" 2>&1 &
NEW_PID=$!

# 记录 PID 到文件
echo "$NEW_PID" > "$PID_FILE"

echo "----------------------------------------"
echo "✅ 应用 ${APP_NAME} 启动成功！进程 PID: $NEW_PID"
echo "💡 提示: 可以通过 tail -f $LOG_FILE 查看实时日志"
echo ""
