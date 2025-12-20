package com.github.axinger.service;

import cn.hutool.core.lang.func.LambdaUtil;
import com.github.axinger.dto.FormBaseDTO;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricProcessInstanceQuery;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentQuery;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.identitylink.api.history.HistoricIdentityLink;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.spring.integration.Flowable;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Flowable通用服务类
 * 封装常用的Flowable操作，体现Flowable的核心特性和使用方法
 */
@Service
public class FlowableService {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private IdentityService identityService;


    /**
     * 部署流程定义 - 通过classpath资源
     *
     * @param classpathResource 流程定义文件路径
     * @param name              部署名称
     * @return 部署对象
     */
    public Deployment deployProcessFromClasspath(String classpathResource, String name) {
        return repositoryService.createDeployment()
                .addClasspathResource(classpathResource)
                .name(name)
                .deploy();
    }

    /**
     * 部署流程定义 - 通过BPMN模型
     *
     * @param model BPMN模型
     * @param name  部署名称
     * @return 部署对象
     */
    public Deployment deployProcessFromModel(BpmnModel model, String name) {
        return repositoryService.createDeployment()
                .addBpmnModel(name + ".bpmn", model)
                .name(name)
                .deploy();
    }

    /**
     * 查询流程定义列表
     *
     * @return 流程定义列表
     */
    public List<ProcessDefinition> getProcessDefinitions() {
        return createProcessDefinitionQuery().list();
    }

    /**
     * 创建流程定义查询对象
     *
     * @return ProcessDefinitionQuery
     */
    public ProcessDefinitionQuery createProcessDefinitionQuery() {
        return repositoryService.createProcessDefinitionQuery();
    }

    /**
     * 创建部署查询对象
     *
     * @return DeploymentQuery
     */
    public DeploymentQuery createDeploymentQuery() {
        return repositoryService.createDeploymentQuery();
    }

    /**
     * 启动流程实例
     *
     * @param processDefinitionKey 流程定义key
     * @param businessKey          业务key
     * @param variables            流程变量
     * @return 流程实例
     */
    public ProcessInstance startProcessInstance(String processDefinitionKey, String businessKey, Map<String, Object> variables) {
        return runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
    }

    /**
     * 启动流程实例
     *
     * @param processDefinitionKey 流程定义key
     * @param variables            流程变量
     * @return 流程实例
     */
    public ProcessInstance startProcessInstance(String processDefinitionKey, Map<String, Object> variables) {


//        return runtimeService.startProcessInstanceByKey(processDefinitionKey, "your-biz-id", variables);

        String currentUserId = variables.get(LambdaUtil.getFieldName(FormBaseDTO::getApplicant)).toString(); // 你系统的用户ID

        // 设置流程变量 + 发起人
        identityService.setAuthenticatedUserId(currentUserId);
        try {
            return runtimeService
                    .createProcessInstanceBuilder()
                    .owner("") //设置的是 流程实例的“拥有者”（OWNER_ 字段）,自定义拥有者（通常用于业务归属，如部门负责人）
                    .processDefinitionKey(processDefinitionKey)
                    .businessKey("your-biz-id") // 如订单ID、请假单ID
                    .variables(variables)
                    .start();
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
    }

    /**
     * 创建流程实例查询对象
     *
     * @return ProcessInstanceQuery
     */
    public ProcessInstanceQuery createProcessInstanceQuery() {
        return runtimeService.createProcessInstanceQuery();
    }

    /**
     * 创建任务查询对象
     *
     * @return TaskQuery
     */
    public TaskQuery createTaskQuery() {
        return taskService.createTaskQuery();
    }

    /**
     * 完成任务
     *
     * @param taskId    任务ID
     * @param variables 任务变量
     */
    public void completeTask(String taskId, Map<String, Object> variables) {
        taskService.complete(taskId, variables);
    }

    /**
     * 完成任务
     *
     * @param taskId 任务ID
     */
    public void completeTask(String taskId) {
        taskService.complete(taskId);
    }

    /**
     * 设置任务受理人
     *
     * @param taskId   任务ID
     * @param assignee 受理人
     */
    public void setAssignee(String taskId, String assignee) {
        taskService.setAssignee(taskId, assignee);
    }

    /**
     * 设置任务负责人
     *
     * @param taskId 任务ID
     * @param owner  负责人
     */
    public void setOwner(String taskId, String owner) {
        taskService.setOwner(taskId, owner);
    }

    /**
     * 添加候选用户
     *
     * @param taskId 任务ID
     * @param userId 用户ID
     */
    public void addCandidateUser(String taskId, String userId) {
        taskService.addCandidateUser(taskId, userId);
    }

    /**
     * 添加候选组
     *
     * @param taskId  任务ID
     * @param groupId 组ID
     */
    public void addCandidateGroup(String taskId, String groupId) {
        taskService.addCandidateGroup(taskId, groupId);
    }

    /**
     * 获取任务的标识链接（包括候选人、候选组等）
     *
     * @param taskId 任务ID
     * @return 标识链接列表
     */
    public List<IdentityLink> getTaskIdentityLinks(String taskId) {
        return taskService.getIdentityLinksForTask(taskId);
    }

    /**
     * 创建历史流程实例查询对象
     *
     * @return HistoricProcessInstanceQuery
     */
    public HistoricProcessInstanceQuery createHistoricProcessInstanceQuery() {
        return historyService.createHistoricProcessInstanceQuery();
    }

    /**
     * 创建历史任务实例查询对象
     *
     * @return HistoricTaskInstanceQuery
     */
    public HistoricTaskInstanceQuery createHistoricTaskInstanceQuery() {
        return historyService.createHistoricTaskInstanceQuery();
    }

    /**
     * 获取历史流程实例变量
     *
     * @param processInstanceId 流程实例ID
     * @return 历史变量列表
     */
    public List<HistoricVariableInstance> getHistoricVariables(String processInstanceId) {
        return historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
    }

    /**
     * 获取历史流程实例的标识链接
     *
     * @param processInstanceId 流程实例ID
     * @return 历史标识链接列表
     */
    public List<HistoricIdentityLink> getHistoricIdentityLinks(String processInstanceId) {
        return historyService.getHistoricIdentityLinksForProcessInstance(processInstanceId);
    }

    /**
     * 删除部署
     *
     * @param deploymentId 部署ID
     * @param cascade      是否级联删除（包括流程实例、历史记录等）
     */
    public void deleteDeployment(String deploymentId, boolean cascade) {
        repositoryService.deleteDeployment(deploymentId, cascade);
    }

    /**
     * 删除流程实例
     *
     * @param processInstanceId 流程实例ID
     * @param deleteReason      删除原因
     */
    public void deleteProcessInstance(String processInstanceId, String deleteReason) {
        runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
    }

    /**
     * 激活流程实例
     *
     * @param processInstanceId 流程实例ID
     */
    public void activateProcessInstance(String processInstanceId) {
        runtimeService.activateProcessInstanceById(processInstanceId);
    }

    /**
     * 挂起流程实例
     *
     * @param processInstanceId 流程实例ID
     */
    public void suspendProcessInstance(String processInstanceId) {
        runtimeService.suspendProcessInstanceById(processInstanceId);
    }

    /**
     * 获取流程图输入流
     *
     * @param processInstanceId 流程实例ID
     * @return 流程图输入流
     */
    public InputStream getProcessDiagram(String processInstanceId) {
        // 获取流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        // 如果流程已结束，则从历史中查找
        if (processInstance == null) {
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            if (historicProcessInstance == null) {
                return null;
            }
            BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
            ProcessDiagramGenerator diagramGenerator = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
            return diagramGenerator.generateDiagram(
                    bpmnModel,
                    "png",
                    historyService.createHistoricActivityInstanceQuery()
                            .processInstanceId(processInstanceId)
                            .list()
                            .stream()
                            .map(activity -> activity.getActivityId())
                            .collect(java.util.stream.Collectors.toList()),
                    java.util.Collections.emptyList(),
                    processEngine.getProcessEngineConfiguration().getActivityFontName(),
                    processEngine.getProcessEngineConfiguration().getLabelFontName(),
                    processEngine.getProcessEngineConfiguration().getAnnotationFontName(),
                    processEngine.getProcessEngineConfiguration().getClassLoader(),
                    1.0,
                    true);
        } else {
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
            ProcessDiagramGenerator diagramGenerator = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();

            // 获取活跃节点
            List<String> activeActivityIds = runtimeService.getActiveActivityIds(
                    runtimeService.createExecutionQuery()
                            .processInstanceId(processInstanceId)
                            .singleResult()
                            .getId()
            );

            return diagramGenerator.generateDiagram(
                    bpmnModel,
                    "png",
                    activeActivityIds,
                    java.util.Collections.emptyList(),
                    processEngine.getProcessEngineConfiguration().getActivityFontName(),
                    processEngine.getProcessEngineConfiguration().getLabelFontName(),
                    processEngine.getProcessEngineConfiguration().getAnnotationFontName(),
                    processEngine.getProcessEngineConfiguration().getClassLoader(),
                    1.0,
                    true);
        }
    }

    /**
     * 获取流程变量
     *
     * @param processInstanceId 流程实例ID
     * @return 流程变量Map
     */
    public Map<String, Object> getProcessVariables(String processInstanceId) {
        return runtimeService.getVariables(processInstanceId);
    }

    /**
     * 设置流程变量
     *
     * @param processInstanceId 流程实例ID
     * @param variables         变量Map
     */
    public void setProcessVariables(String processInstanceId, Map<String, Object> variables) {
        runtimeService.setVariables(processInstanceId, variables);
    }

    /**
     * 获取任务变量
     *
     * @param taskId 任务ID
     * @return 任务变量Map
     */
    public Map<String, Object> getTaskVariables(String taskId) {
        return taskService.getVariables(taskId);
    }

    /**
     * 设置任务变量
     *
     * @param taskId    任务ID
     * @param variables 变量Map
     */
    public void setTaskVariables(String taskId, Map<String, Object> variables) {
        taskService.setVariables(taskId, variables);
    }
}
