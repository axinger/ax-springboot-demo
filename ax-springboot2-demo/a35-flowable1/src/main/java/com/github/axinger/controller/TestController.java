package com.github.axinger.controller;


import cn.hutool.core.io.IoUtil;
import com.github.axinger.vo.TaskVO;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.*;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.image.ProcessDiagramGenerator;
import org.flowable.task.api.Task;
import org.flowable.variable.api.persistence.entity.VariableInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/expense")
public class TestController {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ProcessEngine processEngine;


    @Autowired
    private HistoryService historyService;

/***************此处为业务代码******************/

    /**
     * 添加报销
     *
     * @param userId      用户Id
     * @param money       报销金额
     * @param description 描述
     */
    @RequestMapping(value = "add")
    @ResponseBody
    public Object addExpense(String userId, Integer money, String description) {
        //启动流程
        HashMap<String, Object> map = new HashMap<>();
        map.put("taskUser", userId);
        map.put("money", money);
        map.put("description", description);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Expense", map);

        Map<String, Object> res = new HashMap<>();
        res.put("taskId", processInstance.getId());
        res.put("id", processInstance.getId());
        res.put("name", processInstance.getName());
        res.put("processInstanceId", processInstance.getProcessInstanceId());
        res.put("processDefinitionId", processInstance.getProcessDefinitionId());

        processInstance.getProcessInstanceId();
        return res;
    }

    /**
     * 获取审批管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String userId) {
        List<Task> tasks = taskService.createTaskQuery().taskAssignee(userId).orderByTaskCreateTime().desc().list();
        List<Map<String, Object>> list = new ArrayList<>();

        // 获取流程变量,全量查询
        List<VariableInstance> variableInstanceList = runtimeService.getVariableInstancesByExecutionIds(tasks.stream().map(Task::getProcessInstanceId)
                .collect(Collectors.toSet()));

        for (Task task : tasks) {
            System.out.println(task.toString());
            Map<String, Object> map = new HashMap<>();
            //TaskInfo
            map.put("processInstanceId", task.getProcessInstanceId());
            map.put("id", task.getId());
            map.put("name", task.getName());
            map.put("parentTaskId", task.getParentTaskId());
            map.put("assignee", task.getAssignee());
            map.put("createTime", task.getCreateTime());
            map.put("owner", task.getOwner());
            map.put("description", task.getDescription());
            map.put("delegationState", task.getDelegationState());
            map.put("taskDefinitionKey", task.getTaskDefinitionKey());
            map.put("processDefinitionId", task.getProcessDefinitionId());

            map.put("taskLocalVariables", task.getTaskLocalVariables());
            map.put("processVariables", task.getProcessVariables());
            map.put("caseVariables", task.getCaseVariables());
            map.put("claimTime", task.getClaimTime());


            // 获取流程变量,单次查询
//            Map<String, Object> variablesLocal = runtimeService.getVariablesLocal(task.getProcessInstanceId());
//            map.put("variablesLocal", variablesLocal);


            List<Map<String, Object>> list2 = new ArrayList<>();
            for (VariableInstance variableInstance : variableInstanceList) {

                if (variableInstance.getProcessInstanceId().equals(task.getProcessInstanceId())) {
                    Map<String, Object> map2 = new HashMap<>();
                    map2.put("id", variableInstance.getId());
                    map2.put("name", variableInstance.getName());
                    map2.put("value", variableInstance.getValue());
                    map2.put("textValue", variableInstance.getTextValue());
                    map2.put("scopeId", variableInstance.getScopeId());
                    map2.put("subScopeId", variableInstance.getSubScopeId());
                    map2.put("scopeType", variableInstance.getScopeType());
                    map2.put("processInstanceId", variableInstance.getProcessInstanceId());
                    list2.add(map2);
                }
            }
            map.put("variablesLocal2", list2);
            list.add(map);
        }

        return list;
    }

    @GetMapping("/groupList")
    public Object teacherList(String groupId) {
        //此处.taskCandidateGroup("a")的值“a”即是画流程图时辅导员审批节点"分配用户-候选组"中填写的值
        List<Task> tasks = taskService.createTaskQuery()
                .taskCandidateGroup(groupId)
                .orderByTaskCreateTime()
                .desc()
                .list();
        return TaskVO.fromTask(tasks);
    }

    /**
     * 申请人提交, 审批人审批都是掉这个接口,传值  Task 的id
     *
     * @param taskId 任务ID
     */
    @RequestMapping(value = "apply")
    @ResponseBody
    public String apply(String taskId) {
        // runtimeService.startProcessInstanceByKey 返回的id

        // //此处.taskCandidateGroup("a")的值“a”即是画流程图时辅导员审批节点"分配用户-候选组"中填写的值
//        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("a").list();
//        Task task = taskService.createTaskQuery().taskCandidateGroup("a").taskId(taskId).singleResult();
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task == null) {
            throw new RuntimeException("流程不存在");
        }
        //通过审核
        HashMap<String, Object> map = new HashMap<>();
        map.put("outcome", "通过");
        taskService.complete(taskId, map);
        return "processed ok!";
    }


    /**
     * 拒绝
     */
    @ResponseBody
    @RequestMapping(value = "reject")
    public String reject(String taskId) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("outcome", "驳回");
//        taskService.complete(taskId, map);
        taskService.resolveTask(taskId, map);
        return "reject";
    }


    /**
     * 生成流程图
     *
     * @param processId 任务ID
     */
    @RequestMapping(value = "processDiagram")
    public void genProcessDiagram(HttpServletResponse response, String processId) throws Exception {
        ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processId).singleResult();

        //流程走完的不显示图
        if (pi == null) {
            return;
        }
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        //使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
        String InstanceId = task.getProcessInstanceId();
        List<Execution> executions = runtimeService
                .createExecutionQuery()
                .processInstanceId(InstanceId)
                .list();

        //得到正在执行的Activity的Id
        List<String> activityIds = new ArrayList<>();
        List<String> flows = new ArrayList<>();
        for (Execution exe : executions) {
            List<String> ids = runtimeService.getActiveActivityIds(exe.getId());
            activityIds.addAll(ids);
        }

        //获取流程图
        BpmnModel bpmnModel = repositoryService.getBpmnModel(pi.getProcessDefinitionId());
        ProcessEngineConfiguration engconf = processEngine.getProcessEngineConfiguration();
        ProcessDiagramGenerator diagramGenerator = engconf.getProcessDiagramGenerator();
        InputStream inputStream = diagramGenerator.generateDiagram(bpmnModel, "png", activityIds, flows, engconf.getActivityFontName(), engconf.getLabelFontName(), engconf.getAnnotationFontName(), engconf.getClassLoader(), 1.0, true);
//        OutputStream out = null;
//        byte[] buf = new byte[1024];
//        int legth = 0;
//        try {
//            out = httpServletResponse.getOutputStream();
//            while ((legth = in.read(buf)) != -1) {
//                out.write(buf, 0, legth);
//            }
//        } finally {
//            if (in != null) {
//                in.close();
//            }
//            if (out != null) {
//                out.close();
//            }
//        }

        response.setContentType("image/png");
        IoUtil.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
    }


    @GetMapping("/running")
    public Object getRunningProcessInstances() {
        List<ProcessInstance> list = runtimeService.createProcessInstanceQuery().list();
        return list;
    }

    @GetMapping("/history")
    public ResponseEntity<List<HistoricProcessInstance>> getHistoricProcessInstances() {
        return ResponseEntity.ok(historyService.createHistoricProcessInstanceQuery().list());
    }
}
