package com.github.axinger.controller.api;

import com.github.axinger.domain.A35UserEntity;
import com.github.axinger.service.FlowableService;
import com.github.axinger.service.OrgService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/process-view")
public class ProcessViewController {

    @Autowired
    private FlowableService flowableService;

    @Autowired
    private OrgService orgService;

    /**
     * 启动流程实例
     */
    @PostMapping("/start-process")
    public ResponseEntity<Map<String, Object>> startProcessInstance(
            @RequestParam String processDefinitionKey,
            @RequestBody(required = false) Map<String, Object> variables,
            HttpSession session) {

        A35UserEntity currentUser = (A35UserEntity) session.getAttribute("user");
        Map<String, Object> response = new HashMap<>();

        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "用户未登录");
            return ResponseEntity.status(401).body(response);
        }

        try {
            // 添加申请人信息到变量中
            if (variables == null) {
                variables = new HashMap<>();
            }
            variables.put("applicant", currentUser.getId());

            ProcessInstance processInstance = flowableService.startProcessInstance(
                    processDefinitionKey, variables);

            response.put("success", true);
            response.put("message", "流程启动成功");
            response.put("processInstanceId", processInstance.getId());
            response.put("processDefinitionId", processInstance.getProcessDefinitionId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "启动流程失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 获取可申请的流程列表
     */
    @GetMapping("/available-processes")
    public ResponseEntity<Map<String, Object>> getAvailableProcesses() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<ProcessDefinition> processDefinitions = flowableService.getProcessDefinitions();
            response.put("success", true);
            response.put("processes", processDefinitions);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取流程列表失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 提交任务
     */
    @PostMapping("/complete-task/{taskId}")
    public ResponseEntity<Map<String, Object>> completeTask(
            @PathVariable String taskId,
            @RequestBody(required = false) Map<String, Object> variables,
            HttpSession session) {

        A35UserEntity currentUser = (A35UserEntity) session.getAttribute("user");
        Map<String, Object> response = new HashMap<>();

        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "用户未登录");
            return ResponseEntity.status(401).body(response);
        }

        try {
            Task task = flowableService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null) {
                response.put("success", false);
                response.put("message", "任务不存在");
                return ResponseEntity.status(404).body(response);
            }

            // 检查任务是否属于当前用户
            if (!currentUser.getId().equals(task.getAssignee()) &&
                !flowableService.createTaskQuery().taskId(taskId).taskCandidateUser(currentUser.getId()).list().contains(task)) {
                response.put("success", false);
                response.put("message", "您没有权限处理此任务");
                return ResponseEntity.status(403).body(response);
            }

            if (variables == null) {
                flowableService.completeTask(taskId);
            } else {
                flowableService.completeTask(taskId, variables);
            }

            response.put("success", true);
            response.put("message", "任务处理成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "处理任务失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 认领任务
     */
    @PostMapping("/claim-task/{taskId}")
    public ResponseEntity<Map<String, Object>> claimTask(
            @PathVariable String taskId,
            HttpSession session) {

        A35UserEntity currentUser = (A35UserEntity) session.getAttribute("user");
        Map<String, Object> response = new HashMap<>();

        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "用户未登录");
            return ResponseEntity.status(401).body(response);
        }

        try {
            Task task = flowableService.createTaskQuery().taskId(taskId).singleResult();
            if (task == null) {
                response.put("success", false);
                response.put("message", "任务不存在");
                return ResponseEntity.status(404).body(response);
            }

            flowableService.createTaskQuery().taskId(taskId).taskCandidateUser(currentUser.getId());
            flowableService.setAssignee(taskId, currentUser.getId());

            response.put("success", true);
            response.put("message", "任务认领成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "认领任务失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
