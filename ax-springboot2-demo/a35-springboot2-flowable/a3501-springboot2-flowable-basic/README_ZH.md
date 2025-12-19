# Flowable 工作流引擎使用指南

本文档旨在介绍如何在Spring Boot项目中使用Flowable工作流引擎，展示其核心特性和使用方法。

## Flowable 简介

Flowable 是一个轻量级的业务流程引擎，使用Java编写，基于Apache 2.0许可证开源。它提供了以下核心组件：

1. **BPMN 2.0流程引擎** - 用于执行业务流程
2. **DMN决策表引擎** - 用于业务决策管理
3. **CMMN案例管理引擎** - 用于自适应案例管理
4. **表单引擎** - 用于处理用户表单

## 核心概念

### 1. 流程定义(Process Definition)
流程定义是业务流程的蓝图，通常以BPMN 2.0 XML格式表示。它描述了流程的各个步骤、决策点和流转规则。

### 2. 流程实例(Process Instance)
流程实例是流程定义的一次具体执行。每个流程实例都有唯一的ID，并包含执行过程中的状态信息。

### 3. 任务(Task)
任务是流程执行过程中的工作单元。在Flowable中主要有：
- **用户任务(User Task)** - 需要人工处理的任务
- **服务任务(Service Task)** - 自动执行的服务调用
- **脚本任务(Script Task)** - 执行脚本代码的任务

### 4. 执行(Execution)
执行代表流程实例当前的执行路径。在并行流程中，一个流程实例可能有多个执行。

## 主要功能模块

### 1. 流程定义管理
通过[ProcessDefinitionController](src/main/java/com/github/axinger/controller/ProcessDefinitionController.java)可以实现：
- 部署流程定义(BPMN文件)
- 查询流程定义列表
- 激活/挂起流程定义
- 删除流程定义

### 2. 流程实例管理
通过[ProcessInstanceController](src/main/java/com/github/axinger/controller/ProcessInstanceController.java)可以实现：
- 启动流程实例
- 查询流程实例状态
- 激活/挂起流程实例
- 删除流程实例
- 获取流程变量和历史数据

### 3. 任务管理
通过[TaskManagementController](src/main/java/com/github/axinger/controller/TaskManagementController.java)可以实现：
- 查询任务列表(按处理人、候选人等)
- 完成任务
- 设置任务处理人
- 添加候选用户/组
- 设置任务变量

### 4. 历史数据查询
通过[HistoryController](src/main/java/com/github/axinger/controller/HistoryController.java)可以查询：
- 历史流程实例
- 历史任务实例
- 历史活动实例
- 历史变量数据

## 示例流程

### 1. 员工请假流程(employee-leave.bpmn20.xml)
这是一个典型的分级审批流程：
- 员工提交请假申请
- 根据请假天数自动路由到不同级别的审批人：
  - 1天以内：直属领导审批
  - 1-3天：部门领导审批
  - 3天以上：公司领导审批
- 支持审批通过或驳回，驳回时流程会退回重新申请

### 2. 员工报销流程(reimbursement-process.bpmn20.xml)
这是一个复杂的多级审批流程：
- 员工提交报销申请
- 部门领导初审
- 根据金额大小决定后续审批路径：
  - ≤500元：财务审核后结束
  - 500-5000元：分管副总审核→财务审核
  - >5000元：分管副总审核→总经理审核→财务审核

## 核心服务类

[FlowableService](src/main/java/com/github/axinger/service/FlowableService.java)封装了Flowable的常用操作：
- RepositoryService相关操作(流程定义、部署管理)
- RuntimeService相关操作(流程实例管理)
- TaskService相关操作(任务管理)
- HistoryService相关操作(历史数据查询)

## 关键特性演示

### 1. 动态任务分配
通过TaskListener实现审批人的动态分配，参考[LeaveTaskListener](src/main/java/com/github/axinger/config/LeaveTaskListener.java)

### 2. 条件分支
使用排他网关(Exclusive Gateway)和条件表达式实现不同的审批路径

### 3. 表单属性
在用户任务中定义表单属性，便于前端展示和数据收集

### 4. 流程变量
在整个流程执行过程中传递和使用变量数据

## 前端页面功能

### 1. 登录页面 (login.html)
- 用户登录认证
- 会话管理

### 2. 任务中心 (task.html)
- 展示可申请的流程
- 查看我申请的流程
- 处理待审批任务
- 查看已完结任务

### 3. 流程管理 (process-management.html)
- 流程定义管理
- 流程实例管理
- 任务管理
- 历史记录查询

### 4. 流程监控 (process-monitor.html)
- 流程运行状态统计
- 任务处理时效分析
- 流程执行详情展示

## 使用方法

1. 启动应用后，系统会自动部署resources/processes目录下的流程定义文件
2. 通过REST API进行流程操作：
   - 启动流程实例：POST /api/process-instance/start
   - 查询任务：GET /api/task/assignee/{assignee}
   - 完成任务：POST /api/task/{taskId}/complete
   - 查询历史：GET /api/history/process-instances
3. 通过前端页面进行可视化操作：
   - 登录页面：/login
   - 任务中心：/task
   - 流程管理：/process-management
   - 流程监控：/process-monitor

## 最佳实践

1. 使用流程变量传递业务数据
2. 通过监听器实现任务的自动分配
3. 合理设计流程，避免过于复杂的分支和循环
4. 充分利用历史数据进行流程分析和优化
5. 使用适当的异常处理机制保证流程的稳定性