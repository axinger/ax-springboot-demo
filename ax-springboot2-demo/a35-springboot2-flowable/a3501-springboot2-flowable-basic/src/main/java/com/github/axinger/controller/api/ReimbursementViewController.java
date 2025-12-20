package com.github.axinger.controller.api;

import com.github.axinger.domain.A35UserEntity;
import com.github.axinger.service.FlowableService;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reimbursement-view")
public class ReimbursementViewController {

    @Autowired
    private FlowableService flowableService;

    /**
     * 提交报销申请
     */
    @PostMapping("/apply")
    public ResponseEntity<Map<String, Object>> applyReimbursement(
            @RequestParam BigDecimal amount,
            @RequestParam String title,
            @RequestParam String description,
            HttpSession session) {

        A35UserEntity currentUser = (A35UserEntity) session.getAttribute("user");
        Map<String, Object> response = new HashMap<>();

        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "用户未登录");
            return ResponseEntity.status(401).body(response);
        }

        try {
            // 准备流程变量
            Map<String, Object> variables = new HashMap<>();
            variables.put("applicant", currentUser.getName());
            variables.put("amount", amount);
            variables.put("title", title);
            variables.put("description", description);

            // 启动报销流程
            var processInstance = flowableService.startProcessInstance("expenseReimbursement", variables);

            response.put("success", true);
            response.put("message", "报销申请提交成功");
            response.put("processInstanceId", processInstance.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "提交报销申请失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 审批报销申请
     */
    @PostMapping("/approve/{taskId}")
    public ResponseEntity<Map<String, Object>> approveReimbursement(
            @PathVariable String taskId,
            @RequestParam boolean approved,
            @RequestParam String comment,
            HttpSession session) {

        A35UserEntity currentUser = (A35UserEntity) session.getAttribute("user");
        Map<String, Object> response = new HashMap<>();

        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "用户未登录");
            return ResponseEntity.status(401).body(response);
        }

        try {
            // 签收任务（如果任务还未被签收）
            TaskQuery taskQuery = flowableService.createTaskQuery();
            Task task = taskQuery.taskId(taskId).singleResult();
            if (task != null && task.getAssignee() == null) {
                flowableService.setAssignee(taskId, currentUser.getId().toString());
            }

            // 完成任务
            Map<String, Object> variables = new HashMap<>();
            variables.put("approved", approved);
            variables.put("comment", comment);
            variables.put("approver", currentUser.getName());

            flowableService.completeTask(taskId, variables);

            response.put("success", true);
            response.put("message", "审批操作完成");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "审批操作失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 获取任务详情
     */
    @GetMapping("/task/{taskId}")
    public ResponseEntity<Map<String, Object>> getTaskDetails(@PathVariable String taskId, HttpSession session) {
        A35UserEntity currentUser = (A35UserEntity) session.getAttribute("user");
        Map<String, Object> response = new HashMap<>();

        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "用户未登录");
            return ResponseEntity.status(401).body(response);
        }

        try {
            TaskQuery taskQuery = flowableService.createTaskQuery();
            Task task = taskQuery.taskId(taskId).singleResult();
            if (task == null) {
                response.put("success", false);
                response.put("message", "任务不存在或已完成");
                return ResponseEntity.status(404).body(response);
            }

            // 检查用户是否有权限查看此任务
            boolean hasPermission = false;
            if (task.getAssignee() != null && task.getAssignee().equals(currentUser.getId().toString())) {
                hasPermission = true;
            } else {
                List<IdentityLink> identityLinks = flowableService.getTaskIdentityLinks(taskId);
                for (IdentityLink link : identityLinks) {
                    if (link.getUserId() != null && link.getUserId().equals(currentUser.getId().toString())) {
                        hasPermission = true;
                        break;
                    }
                }
            }

            if (!hasPermission) {
                response.put("success", false);
                response.put("message", "您没有权限查看此任务");
                return ResponseEntity.status(403).body(response);
            }

            // 获取流程变量
            Map<String, Object> processVariables = flowableService.getProcessVariables(task.getProcessInstanceId());

            response.put("success", true);
            response.put("task", task);
            response.put("processVariables", processVariables);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取任务详情失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}