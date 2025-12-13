package com.github.axinger.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceQuery;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/process")
public class ProcessController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProcessEngine processEngine;

    @GetMapping("/running")
    public ResponseEntity<List<ProcessInstance>> getRunningProcessInstances() {
        return ResponseEntity.ok(runtimeService.createProcessInstanceQuery().list());
    }

    @GetMapping("/history")
    public ResponseEntity<List<HistoricProcessInstance>> getHistoricProcessInstances() {
        return ResponseEntity.ok(historyService.createHistoricProcessInstanceQuery().list());
    }


    /**
     * 获取审批管理列表
     */
    @RequestMapping(value = "/list")
    public Object list(String userId) {
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().desc().list();

        return tasks.stream().map(task -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", task.getId());
            map.put("processInstanceId", task.getProcessInstanceId());
            map.put("name", task.getName());
            map.put("assignee", task.getAssignee());
            map.put("description", task.getDescription());
            map.put("delegationState", task.getDelegationState());
            return map;
        }).collect(Collectors.toSet());
    }

    //1. 使用RuntimeService获取运行中的流程实例
    public List<ProcessInstance> getAllRunningProcessInstances() {
        return runtimeService.createProcessInstanceQuery().list();
    }

    //2. 获取所有流程定义
    public List<ProcessDefinition> getAllProcessDefinitions() {
        return repositoryService.createProcessDefinitionQuery().list();
    }

    //3. 获取历史流程实例（包括已完成的）

    public List<HistoricProcessInstance> getAllHistoricProcessInstances() {
        return historyService.createHistoricProcessInstanceQuery().list();
    }

    //4. 分页查询流程实例
    public Page<ProcessInstance> getProcessInstancesByPage(int page, int size) {
        long total = runtimeService.createProcessInstanceQuery().count();
        List<ProcessInstance> instances = runtimeService.createProcessInstanceQuery()
                .orderByProcessInstanceId().desc()
                .listPage(page * size, size);

        Page<ProcessInstance> objectPage1 = Page.of(page, size, total);
        objectPage1.setRecords(instances);

        return objectPage1;
    }

    //5. 带条件的流程实例查询
    public List<ProcessInstance> getProcessInstancesByCondition(String processDefinitionKey,
                                                                String businessKey,
                                                                String applicant) {
        ProcessInstanceQuery query = runtimeService.createProcessInstanceQuery();

        if (StringUtils.isNotBlank(processDefinitionKey)) {
            query.processDefinitionKey(processDefinitionKey);
        }

        if (StringUtils.isNotBlank(businessKey)) {
            query.processInstanceBusinessKey(businessKey);
        }

        if (StringUtils.isNotBlank(applicant)) {
            query.variableValueEquals("applicant", applicant);
        }

        return query.list();
    }

    //6. 获取流程实例详情（包含变量）
    public Map<String, Object> getProcessInstanceDetail(String processInstanceId) {
        ProcessInstance instance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        if (instance == null) {
            throw new RuntimeException("流程实例不存在");
        }

        Map<String, Object> result = new HashMap<>();
        result.put("processInstance", instance);
        result.put("variables", runtimeService.getVariables(processInstanceId));

        return result;
    }


    /**
     * 生成流程图
     *
     * @param processId 流程ID,不是任务id
     */
    @RequestMapping(value = "processDiagram")
    public void genProcessDiagram(HttpServletResponse httpServletResponse, String processId) throws Exception {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();

        // 流程走完的不显示图
        if (pi == null) {
            return;
        }
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        // 使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        String InstanceId = task.getProcessInstanceId();
        List<Execution> executions = runtimeService
                .createExecutionQuery()
                .processInstanceId(InstanceId)
                .list();

        // 得到正在执行的Activity的Id
        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }

        // 获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
        byte[] buf = new byte[1024];
        int legth = 0;
        try (InputStream in = diagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows,
                engconf.getActivityFontName(), engconf.getLabelFontName(),
                engconf.getAnnotationFontName(), engconf.getClassLoader(), 1.0, true); OutputStream out = httpServletResponse.getOutputStream()) {
            while ((legth = in.read(buf)) != -1) {
                out.write(buf, 0, legth);
            }
        }
    }
}
