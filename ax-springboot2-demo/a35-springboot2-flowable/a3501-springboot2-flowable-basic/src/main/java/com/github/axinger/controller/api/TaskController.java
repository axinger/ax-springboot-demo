package com.github.axinger.controller.api;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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


        List<Map<String, Object>> myApplications = new ArrayList<>();
        // 2. 我发起的流程（已申请的）
        List<ProcessInstance> myProcessInstances = runtimeService.createProcessInstanceQuery()
                .startedBy(userId)
                .list();

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

        List<HistoricProcessInstance> myHistoricProcessInstances = historyService.createHistoricProcessInstanceQuery()
                .startedBy(userId)
                .finished()
                .list();
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

        List<Task> candidateTasks = taskService.createTaskQuery()
                .taskCandidateUser(userId)
                .list();
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

    /**
     * 提交流程实例审批
     *
     * @param processInstanceId 流程实例ID
     * @param userId            用户ID
     * @return 操作结果
     */
    @PostMapping("/submit-for-approval")
    public ResponseEntity<Map<String, Object>> submitForApproval(
            @RequestParam String processInstanceId,
            @RequestParam String userId) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 检查流程实例是否存在且由该用户发起
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .startedBy(userId)
                    .singleResult();

            if (processInstance == null) {
                response.put("success", false);
                response.put("message", "未找到由您发起的该流程实例");
                return ResponseEntity.badRequest().body(response);
            }

            // 检查流程是否已经处于审批状态
            // 这里可以根据实际业务需求进行判断
            // 例如检查当前活动节点是否是申请人节点等

            // 如果需要触发某个特定任务，可以在这里添加逻辑
            // 比如完成某个特定的任务

//            TaskQuery taskQuery = taskService.createTaskQuery();
//            Task task = taskQuery.taskId(processInstance.get).singleResult();
//            if (task != null && task.getAssignee() == null) {
//                flowableService.setAssignee(taskId, currentUser.getId().toString());
//            }
            Map<String, Object> variables = new HashMap<>();
//            variables.put("approved", approved);
//            variables.put("comment", comment);
//            variables.put("approver", currentUser.getName());

//            runtimeService
//                    .createProcessInstanceBuilder()
//                    .owner("") //设置的是 流程实例的“拥有者”（OWNER_ 字段）,自定义拥有者（通常用于业务归属，如部门负责人）
//                    .processDefinitionKey(processInstanceId)
//                    .businessKey("your-biz-id") // 如订单ID、请假单ID
//                    .variables(variables)
//                    .start();
//            taskService.complete(processInstanceId, variables);
//            runtimeService.setAssignee(processInstanceId, userId);


            response.put("success", true);
            response.put("message", "流程已成功提交审批");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "提交审批失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
