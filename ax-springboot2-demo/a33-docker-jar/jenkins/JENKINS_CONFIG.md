# 🚀 Jenkins CI/CD 配置指南

## 📋 概述

本目录包含完整的 Jenkins 自动化部署脚本，支持：
- ✅ 多模块依赖管理
- ✅ 自动化构建和测试
- ✅ 多服务器并行/串行部署
- ✅ 重试机制和错误处理
- ✅ 详细的日志输出和状态检查

---

## 📂 文件说明

| 文件名 | 用途 | 执行阶段 |
|--------|------|---------|
| `jenkins-Pre Steps.sh` | 预构建步骤：编译依赖模块 | Build 前 |
| `jenkins-Post Steps.sh` | 部署步骤：发布到远程服务器 | Build 后 |
| `JENKINS_CONFIG.md` | 本文档：配置说明 | - |

---

## 🔧 Jenkins Job 配置

### 1. 创建 FreeStyle Project

```
Jenkins Dashboard → New Item → FreeStyle Project
```

### 2. 源码管理 (Source Code Management)

```yaml
Git:
  Repository URL: git@your-repo.git
  Branch: */main
  Credentials: 选择你的 Git 凭证
```

### 3. 构建环境 (Build Environment)

勾选以下选项：
- ✅ Add timestamps to the Console Output
- ✅ Use secret text(s) or file(s) (如需使用凭证)

### 4. Pre Steps (预构建步骤)

**添加 Execute shell:**

```bash
# 复制 jenkins-Pre Steps.sh 的内容
# 或指向脚本文件
bash ${WORKSPACE}/a33-docker-jar/jenkins/jenkins-Pre\ Steps.sh
```

**配置说明：**
- 自动编译并安装公共模块到本地 Maven 仓库
- 确保主项目可以正确引用依赖

### 5. Build (构建步骤)

**添加 Invoke top-level Maven targets:**

```yaml
Maven Version: 选择你的 Maven 版本
Goals: clean package -DskipTests
POM: pom.xml
```

或使用 **Execute shell:**

```bash
mvn clean package -DskipTests
```

### 6. Post Steps (部署步骤)

**添加 Execute shell:**

```bash
# 复制 jenkins-Post Steps.sh 的内容
# 或指向脚本文件
bash ${WORKSPACE}/a33-docker-jar/jenkins/jenkins-Post\ Steps.sh
```

---

## ⚙️ Jenkins 参数配置

在 Job 配置中勾选 **"This project is parameterized"**，添加以下参数：

### 必需参数

#### 1. target_ip (String Parameter)
- **描述**: 目标服务器 IP 地址（多个用逗号分隔）
- **默认值**: `192.168.1.100,192.168.1.101`
- **示例**: 
  - 单台: `192.168.1.100`
  - 多台: `192.168.1.100,192.168.1.101,192.168.1.102`

#### 2. jar_file_name (String Parameter)
- **描述**: JAR 包文件名
- **默认值**: `demo-application.jar`
- **说明**: 如果不设置，脚本会自动查找最新的 JAR 包

### 可选参数

#### 3. ssh_port (String Parameter)
- **描述**: SSH 端口号
- **默认值**: `22`

#### 4. deploy_user (String Parameter)
- **描述**: 部署用户
- **默认值**: `deploy_user`

#### 5. STRICT_MODE (Boolean Parameter)
- **描述**: 严格模式（Pre Steps 遇到失败立即退出）
- **默认值**: `false`

---

## 🖥️ 服务器准备

### 1. 创建部署用户

```bash
# 在所有目标服务器上执行
sudo useradd -m -s /bin/bash deploy_user
sudo passwd deploy_user

# 添加到 sudo 组（如需要）
sudo usermod -aG sudo deploy_user
```

### 2. 配置 SSH 免密登录

```bash
# 在 Jenkins 服务器上执行
ssh-keygen -t rsa -b 4096 -C "jenkins@ci-server"

# 复制公钥到所有目标服务器
ssh-copy-id -p 22 deploy_user@192.168.1.100
ssh-copy-id -p 22 deploy_user@192.168.1.101
```

### 3. 创建应用目录

```bash
# 在所有目标服务器上执行
sudo mkdir -p /opt/application/demo-app
sudo chown -R deploy_user:deploy_user /opt/application/demo-app
```

### 4. 上传启动脚本

将 `bin/start.sh` 和 `bin/stop.sh` 上传到服务器的 `/opt/application/demo-app/` 目录：

```bash
scp bin/start.sh deploy_user@192.168.1.100:/opt/application/demo-app/
scp bin/stop.sh deploy_user@192.168.1.100:/opt/application/demo-app/

# 设置执行权限
ssh deploy_user@192.168.1.100 "chmod +x /opt/application/demo-app/*.sh"
```

### 5. 配置 .env 文件

在服务器上创建 `/opt/application/demo-app/.env`：

```bash
JAVA_VERSION=17
APP_NAME=demo-application
MODE=cluster
SPRING_CLOUD_NACOS_CONFIG_SERVER_ADDR=nacos-server:8848
SPRING_CLOUD_NACOS_DISCOVERY_SERVER_ADDR=nacos-server:8848
```

---

## 🔄 部署流程

```
┌─────────────────────────────────────┐
│  1. Jenkins 触发构建                 │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  2. Pre Steps: 编译依赖模块          │
│     - 检查 Maven                     │
│     - 遍历依赖模块                   │
│     - mvn clean install              │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  3. Build: 编译主项目                │
│     - mvn clean package              │
│     - 生成 JAR 包                    │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  4. Post Steps: 部署到远程服务器     │
│     ├─ 传输 JAR 包                   │
│     ├─ 停止旧服务 (stop.sh)          │
│     ├─ 启动新服务 (start.sh)         │
│     └─ 验证服务状态                  │
└──────────────┬──────────────────────┘
               │
               ▼
┌─────────────────────────────────────┐
│  5. 部署完成                         │
│     - 显示统计信息                   │
│     - 发送通知（可选）               │
└─────────────────────────────────────┘
```

---

## 📊 日志示例

### Pre Steps 日志

```
[2024-01-15 10:30:00.123 CST] =========================================
[2024-01-15 10:30:00.124 CST] 🚀 开始批量构建依赖模块 (北京时区: Asia/Shanghai)
[2024-01-15 10:30:00.125 CST] 包含模块: ax-enterprise-common/common-core
[2024-01-15 10:30:00.126 CST] Maven 命令: mvn clean install -B -DskipTests
[2024-01-15 10:30:00.127 CST] =========================================
[2024-01-15 10:30:00.200 CST] ✅ Maven 版本: Apache Maven 3.8.6
[2024-01-15 10:30:00.201 CST] 📂 工作空间: /var/lib/jenkins/workspace/demo-app
[2024-01-15 10:30:00.202 CST] 
[2024-01-15 10:30:00.203 CST] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
[2024-01-15 10:30:00.204 CST] 📦 处理模块: ax-enterprise-common/common-core
[2024-01-15 10:30:00.205 CST] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
[2024-01-15 10:30:00.206 CST] ⏳ 正在构建: ax-enterprise-common/common-core ...
...
[2024-01-15 10:30:15.500 CST] ✅ 成功: ax-enterprise-common/common-core 构建完成 (耗时: 15秒)
[2024-01-15 10:30:15.501 CST] 
[2024-01-15 10:30:15.502 CST] =========================================
[2024-01-15 10:30:15.503 CST] 📊 构建结果统计
[2024-01-15 10:30:15.504 CST] =========================================
[2024-01-15 10:30:15.505 CST] 总模块数: 1
[2024-01-15 10:30:15.506 CST] 成功: 1
[2024-01-15 10:30:15.507 CST] 失败: 0
[2024-01-15 10:30:15.508 CST] =========================================
[2024-01-15 10:30:15.509 CST] 🎉 所有预构建步骤成功完成！
```

### Post Steps 日志

```
[2024-01-15 10:35:00.100 CST] =========================================
[2024-01-15 10:35:00.101 CST] 🚀 开始部署应用到远程服务器
[2024-01-15 10:35:00.102 CST] =========================================
[2024-01-15 10:35:00.103 CST] 📦 JAR 包: demo-application.jar (大小: 45M)
[2024-01-15 10:35:00.104 CST] 🖥️ 目标服务器: 192.168.1.100 192.168.1.101
[2024-01-15 10:35:00.105 CST] 📊 部署计划: 2 台服务器
[2024-01-15 10:35:00.106 CST] 
[2024-01-15 10:35:00.107 CST] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
[2024-01-15 10:35:00.108 CST] 🖥️ 正在部署到服务器: 192.168.1.100
[2024-01-15 10:35:00.109 CST] ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
[2024-01-15 10:35:00.110 CST] [1/4] 📤 正在传输文件...
[2024-01-15 10:35:05.200 CST] ✅ 文件传输完成 (耗时: 5秒)
[2024-01-15 10:35:05.201 CST] [2/4] 🛑 正在停止旧服务...
[2024-01-15 10:35:07.300 CST] ✅ 旧服务已停止
[2024-01-15 10:35:07.301 CST] ⏳ 等待服务完全停止...
[2024-01-15 10:35:12.400 CST] [3/4] ▶️ 正在启动新服务...
[2024-01-15 10:35:12.500 CST] ✅ 新服务启动命令已执行
[2024-01-15 10:35:12.501 CST] ⏳ 等待服务启动...
[2024-01-15 10:35:22.600 CST] [4/4] 🔍 正在检查服务状态...
[2024-01-15 10:35:22.700 CST] ✅ 服务在 192.168.1.100 上运行正常 (PID: 12345)
[2024-01-15 10:35:22.701 CST] -----------------------------------------
[2024-01-15 10:35:22.702 CST] ✅ 192.168.1.100 部署完成 (总耗时: 22秒)
...
[2024-01-15 10:36:00.000 CST] =========================================
[2024-01-15 10:36:00.001 CST] 📊 部署结果统计
[2024-01-15 10:36:00.002 CST] =========================================
[2024-01-15 10:36:00.003 CST] 总服务器数: 2
[2024-01-15 10:36:00.004 CST] 成功: 2
[2024-01-15 10:36:00.005 CST] 失败: 0
[2024-01-15 10:36:00.006 CST] =========================================
[2024-01-15 10:36:00.007 CST] 🎉 所有服务器部署完成！
```

---

## 🔍 故障排查

### 问题 1: Maven 构建失败

**症状**: Pre Steps 报错 "Maven 未安装"

**解决**:
```bash
# 在 Jenkins 服务器上安装 Maven
sudo apt install maven  # Ubuntu/Debian
sudo yum install maven  # CentOS/RHEL

# 验证安装
mvn -version
```

### 问题 2: SSH 连接失败

**症状**: Post Steps 报错 "Permission denied" 或 "Connection refused"

**解决**:
```bash
# 1. 检查 SSH 密钥
ssh -p 22 deploy_user@192.168.1.100

# 2. 重新配置免密登录
ssh-copy-id -p 22 deploy_user@192.168.1.100

# 3. 检查防火墙
sudo ufw allow 22/tcp  # Ubuntu
sudo firewall-cmd --add-port=22/tcp --permanent  # CentOS
```

### 问题 3: 服务启动失败

**症状**: 部署后进程检查失败

**解决**:
```bash
# 1. 登录服务器查看日志
ssh deploy_user@192.168.1.100
cd /opt/application/demo-app
tail -f *.log

# 2. 手动测试启动脚本
./start.sh

# 3. 检查 Java 环境
java -version

# 4. 检查端口占用
netstat -tlnp | grep 8080
```

### 问题 4: 权限不足

**症状**: "Permission denied" 错误

**解决**:
```bash
# 在目标服务器上执行
sudo chown -R deploy_user:deploy_user /opt/application/demo-app
chmod +x /opt/application/demo-app/*.sh
```

---

## 💡 最佳实践

### 1. 使用 Jenkins Pipeline（推荐）

相比 FreeStyle Project，Pipeline 更灵活：

```groovy
pipeline {
    agent any
    
    parameters {
        string(name: 'target_ip', defaultValue: '192.168.1.100', description: '目标服务器IP')
        string(name: 'jar_file_name', defaultValue: 'demo-application.jar', description: 'JAR包名称')
    }
    
    stages {
        stage('Pre Build') {
            steps {
                sh 'bash jenkins/jenkins-Pre\\ Steps.sh'
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        
        stage('Deploy') {
            steps {
                sh 'bash jenkins/jenkins-Post\\ Steps.sh'
            }
        }
    }
    
    post {
        success {
            echo '🎉 部署成功！'
        }
        failure {
            echo '❌ 部署失败！'
        }
    }
}
```

### 2. 配置邮件通知

在 Job 配置中添加：
- Editable Email Notification
- 配置收件人列表
- 设置触发条件（Failure, Success）

### 3. 集成钉钉/企业微信通知

使用 Webhook 插件发送部署通知。

### 4. 定期清理

配置 Jenkins 自动清理旧的构建：
- Discard old builds
- Max # of builds to keep: 10
- Max # of days to keep: 30

---

## 📚 相关文档

- [Jenkins 官方文档](https://www.jenkins.io/doc/)
- [Maven 构建指南](https://maven.apache.org/guides/)
- [SSH 免密登录配置](https://linuxize.com/post/how-to-setup-passwordless-ssh-login/)
- [Spring Boot 部署最佳实践](https://docs.spring.io/spring-boot/docs/current/reference/html/deployment.html)

---

## ✅ 快速检查清单

部署前确认：

- [ ] Jenkins 服务器已安装 Maven
- [ ] Jenkins 服务器已配置 Git 凭证
- [ ] 目标服务器已创建部署用户
- [ ] 已配置 SSH 免密登录
- [ ] 目标服务器已安装 Java
- [ ] 已上传 start.sh 和 stop.sh 到目标服务器
- [ ] 已配置 .env 文件
- [ ] 应用目录权限正确
- [ ] Jenkins Job 参数配置正确
- [ ] 防火墙允许 SSH 连接

完成以上检查后，即可开始自动化部署！🚀
