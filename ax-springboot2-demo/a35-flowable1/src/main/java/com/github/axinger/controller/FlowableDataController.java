package com.github.axinger.controller;

import com.github.axinger.entity.User;
import lombok.RequiredArgsConstructor;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/flowable")
@RequiredArgsConstructor
public class FlowableDataController {

    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final RepositoryService repositoryService;
    private final HistoryService historyService;

    /**
     * 查询当前用户待办任务
     */
    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getCurrentUserTasks(HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return ResponseEntity.status(401).build();
        }

        String username = currentUser.getUsername();
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(username)
                .orderByTaskCreateTime()
                .desc()
                .list();

        return ResponseEntity.ok(tasks);
    }

    /**
     * 查询流程定义列表
     */
    @GetMapping("/process-definitions")
    public ResponseEntity<List<ProcessDefinition>> getProcessDefinitions() {
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .orderByProcessDefinitionName()
                .asc()
                .list();

        return ResponseEntity.ok(processDefinitions);
    }

    /**
     * 查询正在运行的流程实例
     */
    @GetMapping("/process-instances")
    public ResponseEntity<List<ProcessInstance>> getRunningProcessInstances() {
        List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
                .orderByStartTime()
                .desc()
                .list();

        return ResponseEntity.ok(processInstances);
    }

    /**
     * 查询历史流程实例
     */
    @GetMapping("/historic-process-instances")
    public ResponseEntity<List<HistoricProcessInstance>> getHistoricProcessInstances(
            @RequestParam(required = false) String businessKey) {
        org.flowable.engine.history.HistoricProcessInstanceQuery query = historyService.createHistoricProcessInstanceQuery();

        if (businessKey != null && !businessKey.isEmpty()) {
            query.processInstanceBusinessKey(businessKey);
        }

        List<HistoricProcessInstance> historicProcessInstances = query
                .orderByProcessInstanceStartTime()
                .desc()
                .list();

        return ResponseEntity.ok(historicProcessInstances);
    }

    /**
     * 根据流程实例ID查询任务列表
     */
    @GetMapping("/tasks-by-process-instance")
    public ResponseEntity<List<Task>> getTasksByProcessInstanceId(@RequestParam String processInstanceId) {
        List<Task> tasks = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .orderByTaskCreateTime()
                .desc()
                .list();

        return ResponseEntity.ok(tasks);
    }

    /**
     * 查询指定用户发起的历史流程实例
     */
    @GetMapping("/historic-process-instances-by-user")
    public ResponseEntity<List<HistoricProcessInstance>> getHistoricProcessInstancesByUser(
            @RequestParam String username) {
        List<HistoricProcessInstance> historicProcessInstances = historyService.createHistoricProcessInstanceQuery()
                .startedBy(username)
                .orderByProcessInstanceStartTime()
                .desc()
                .list();

        return ResponseEntity.ok(historicProcessInstances);
    }
}
