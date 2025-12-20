package com.github.axinger.controller.api;

import com.github.axinger.service.FlowableService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.variable.api.history.HistoricVariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 历史记录控制器
 * 展示Flowable历史数据查询功能
 */
@RestController
@RequestMapping("/api/history")
public class HistoryController {

    @Autowired
    private FlowableService flowableService;

    /**
     * 获取历史流程实例列表
     *
     * @return 历史流程实例列表
     */
    @GetMapping("/process-instances")
    public List<HistoricProcessInstance> getHistoricProcessInstances() {
        return flowableService.createHistoricProcessInstanceQuery().list();
    }

    /**
     * 根据流程实例ID获取历史流程实例
     *
     * @param processInstanceId 流程实例ID
     * @return 历史流程实例
     */
    @GetMapping("/process-instance/{processInstanceId}")
    public HistoricProcessInstance getHistoricProcessInstance(@PathVariable String processInstanceId) {
        return flowableService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
    }

    /**
     * 获取已完成的历史流程实例列表
     *
     * @return 已完成的历史流程实例列表
     */
    @GetMapping("/process-instances/finished")
    public List<HistoricProcessInstance> getFinishedHistoricProcessInstances() {
        return flowableService.createHistoricProcessInstanceQuery()
                .finished()
                .list();
    }

    /**
     * 获取未完成的历史流程实例列表
     *
     * @return 未完成的历史流程实例列表
     */
    @GetMapping("/process-instances/unfinished")
    public List<HistoricProcessInstance> getUnfinishedHistoricProcessInstances() {
        return flowableService.createHistoricProcessInstanceQuery()
                .unfinished()
                .list();
    }

    /**
     * 根据流程定义key获取历史流程实例列表
     *
     * @param processDefinitionKey 流程定义key
     * @return 历史流程实例列表
     */
    @GetMapping("/process-instances/key/{processDefinitionKey}")
    public List<HistoricProcessInstance> getHistoricProcessInstancesByKey(@PathVariable String processDefinitionKey) {
        return flowableService.createHistoricProcessInstanceQuery()
                .processDefinitionKey(processDefinitionKey)
                .list();
    }

    /**
     * 根据业务key获取历史流程实例列表
     *
     * @param businessKey 业务key
     * @return 历史流程实例列表
     */
    @GetMapping("/process-instances/business-key/{businessKey}")
    public List<HistoricProcessInstance> getHistoricProcessInstancesByBusinessKey(@PathVariable String businessKey) {
        return flowableService.createHistoricProcessInstanceQuery()
                .processInstanceBusinessKey(businessKey)
                .list();
    }

    /**
     * 获取历史任务实例列表
     *
     * @return 历史任务实例列表
     */
    @GetMapping("/task-instances")
    public List<HistoricTaskInstance> getHistoricTaskInstances() {
        return flowableService.createHistoricTaskInstanceQuery().list();
    }

    /**
     * 根据任务ID获取历史任务实例
     *
     * @param taskId 任务ID
     * @return 历史任务实例
     */
    @GetMapping("/task-instance/{taskId}")
    public HistoricTaskInstance getHistoricTaskInstance(@PathVariable String taskId) {
        return flowableService.createHistoricTaskInstanceQuery()
                .taskId(taskId)
                .singleResult();
    }

    /**
     * 根据流程实例ID获取历史任务实例列表
     *
     * @param processInstanceId 流程实例ID
     * @return 历史任务实例列表
     */
    @GetMapping("/task-instances/process-instance/{processInstanceId}")
    public List<HistoricTaskInstance> getHistoricTaskInstancesByProcessInstanceId(@PathVariable String processInstanceId) {
        return flowableService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
    }

    /**
     * 获取已完成的历史任务实例列表
     *
     * @return 已完成的历史任务实例列表
     */
    @GetMapping("/task-instances/finished")
    public List<HistoricTaskInstance> getFinishedHistoricTaskInstances() {
        return flowableService.createHistoricTaskInstanceQuery()
                .finished()
                .list();
    }

    /**
     * 获取未完成的历史任务实例列表
     *
     * @return 未完成的历史任务实例列表
     */
    @GetMapping("/task-instances/unfinished")
    public List<HistoricTaskInstance> getUnfinishedHistoricTaskInstances() {
        return flowableService.createHistoricTaskInstanceQuery()
                .unfinished()
                .list();
    }

    /**
     * 根据任务定义key获取历史任务实例列表
     *
     * @param taskDefinitionKey 任务定义key
     * @return 历史任务实例列表
     */
    @GetMapping("/task-instances/key/{taskDefinitionKey}")
    public List<HistoricTaskInstance> getHistoricTaskInstancesByKey(@PathVariable String taskDefinitionKey) {
        return flowableService.createHistoricTaskInstanceQuery()
                .taskDefinitionKey(taskDefinitionKey)
                .list();
    }

    /**
     * 获取历史活动实例列表
     *
     * @return 历史活动实例列表
     */
    @GetMapping("/activity-instances")
    public List<HistoricProcessInstance> getHistoricActivityInstances() {
        List<HistoricProcessInstance> historicProcessInstances = flowableService.createHistoricProcessInstanceQuery()
                .list()
                .stream()
                .flatMap(instance -> flowableService.createHistoricProcessInstanceQuery()
                        .processInstanceId(instance.getId())
                        .list()
                        .stream())
                .findFirst()
                .map(instance -> flowableService.createHistoricProcessInstanceQuery()
                        .processInstanceId(instance.getId())
                        .list()).orElse(new ArrayList<>());
        return historicProcessInstances;

    }

    /**
     * 根据流程实例ID获取历史活动实例列表
     *
     * @param processInstanceId 流程实例ID
     * @return 历史活动实例列表
     */
    @GetMapping("/activity-instances/process-instance/{processInstanceId}")
    public List<HistoricProcessInstance> getHistoricActivityInstancesByProcessInstanceId(@PathVariable String processInstanceId) {
       return flowableService.createHistoricProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
    }

    /**
     * 获取历史变量实例列表
     *
     * @return 历史变量实例列表
     */
    @GetMapping("/variable-instances")
    public List<HistoricVariableInstance> getHistoricVariableInstances() {
        return flowableService.createHistoricProcessInstanceQuery()
                .list()
                .stream()
                .flatMap(instance -> flowableService.getHistoricVariables(instance.getId()).stream())
                .toList();
    }

    /**
     * 根据流程实例ID获取历史变量实例列表
     *
     * @param processInstanceId 流程实例ID
     * @return 历史变量实例列表
     */
    @GetMapping("/variable-instances/process-instance/{processInstanceId}")
    public List<HistoricVariableInstance> getHistoricVariableInstancesByProcessInstanceId(@PathVariable String processInstanceId) {
        return flowableService.getHistoricVariables(processInstanceId);
    }

    /**
     * 根据变量名获取历史变量实例列表
     *
     * @param variableName 变量名
     * @return 历史变量实例列表
     */
    @GetMapping("/variable-instances/name/{variableName}")
    public List<HistoricVariableInstance> getHistoricVariableInstancesByName(@PathVariable String variableName) {
        return flowableService.createHistoricProcessInstanceQuery()
                .list()
                .stream()
                .flatMap(instance -> flowableService.getHistoricVariables(instance.getId()).stream())
                .filter(variable -> variableName.equals(variable.getVariableName()))
                .toList();
    }
}
