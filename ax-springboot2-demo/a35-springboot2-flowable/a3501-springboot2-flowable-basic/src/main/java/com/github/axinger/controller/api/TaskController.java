package com.github.axinger.controller.api;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private HistoryService historyService;

    /**
     * 获取任务主页数据
     */
    @GetMapping("/dashboard")
    public Map<String, Object> getTaskDashboard(String userId) {
        if (userId == null) {
            throw new RuntimeException("用户未登录");
        }

        Map<String, Object> result = new HashMap<>();

        // 1. 可填写的流程（已部署的流程定义）
        List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery()
                .latestVersion()
                .list();
        List<Map<String, Object>> availableProcesses = processDefinitions.stream().map(pd -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", pd.getId());
            map.put("name", pd.getName());
            map.put("key", pd.getKey());
            map.put("version", pd.getVersion());
            map.put("description", pd.getDescription());
            return map;
        }).collect(Collectors.toList());
        result.put("availableProcesses", availableProcesses);

        // 2. 我发起的流程（已申请的）
        List<ProcessInstance> myProcessInstances = runtimeService.createProcessInstanceQuery()
                .startedBy(userId)
                .list();
        List<HistoricProcessInstance> myHistoricProcessInstances = historyService.createHistoricProcessInstanceQuery()
                .startedBy(userId)
                .finished()
                .list();

        List<Map<String, Object>> myApplications = new ArrayList<>();
        // 添加运行中的流程实例
        myProcessInstances.forEach(pi -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", pi.getId());
            map.put("name", pi.getName());
            map.put("processDefinitionName", pi.getProcessDefinitionName());
            map.put("startTime", pi.getStartTime());
            map.put("status", "运行中");
            map.put("businessKey", pi.getBusinessKey());
            myApplications.add(map);
        });
        // 添加已完成的流程实例
        myHistoricProcessInstances.forEach(hpi -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", hpi.getId());
            map.put("name", hpi.getName());
            map.put("processDefinitionName", hpi.getProcessDefinitionName());
            map.put("startTime", hpi.getStartTime());
            map.put("endTime", hpi.getEndTime());
            map.put("status", "已完成");
            map.put("businessKey", hpi.getBusinessKey());
            myApplications.add(map);
        });
        result.put("myApplications", myApplications);

        // 3. 待我审批的任务（待审批的）
        List<Task> assignedTasks = taskService.createTaskQuery()
                .taskAssignee(userId)
                .list();
        List<Task> candidateTasks = taskService.createTaskQuery()
                .taskCandidateUser(userId)
                .list();

        List<Map<String, Object>> pendingApprovals = new ArrayList<>();
        assignedTasks.forEach(task -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", task.getId());
            map.put("name", task.getName());
            map.put("processInstanceId", task.getProcessInstanceId());
            map.put("createTime", task.getCreateTime());
            map.put("assignee", task.getAssignee());
            map.put("description", task.getDescription());
            map.put("status", "指派给我");
            pendingApprovals.add(map);
        });

        candidateTasks.forEach(task -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", task.getId());
            map.put("name", task.getName());
            map.put("processInstanceId", task.getProcessInstanceId());
            map.put("createTime", task.getCreateTime());
            map.put("assignee", task.getAssignee());
            map.put("description", task.getDescription());
            map.put("status", "候选人任务");
            pendingApprovals.add(map);
        });
        result.put("pendingApprovals", pendingApprovals);

        // 4. 我参与过的已完成任务（已完结的）
        List<HistoricTaskInstance> finishedTasks = historyService.createHistoricTaskInstanceQuery()
                .taskInvolvedUser(userId)
                .finished()
                .list();
        List<Map<String, Object>> finishedTasksList = finishedTasks.stream().map(hti -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", hti.getId());
            map.put("name", hti.getName());
            map.put("processInstanceId", hti.getProcessInstanceId());
            map.put("startTime", hti.getStartTime());
            map.put("endTime", hti.getEndTime());
            map.put("assignee", hti.getAssignee());
            map.put("durationInMillis", hti.getDurationInMillis());
            return map;
        }).collect(Collectors.toList());
        result.put("finishedTasks", finishedTasksList);

        return result;
    }
}
