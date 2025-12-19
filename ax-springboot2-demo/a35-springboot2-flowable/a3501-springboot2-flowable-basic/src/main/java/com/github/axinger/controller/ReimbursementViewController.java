package com.github.axinger.controller;

import com.github.axinger.domain.A35UserEntity;
import com.github.axinger.service.FlowableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.HashMap;
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
            variables.put("applicant", currentUser.getId());
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
            // 完成任务
            Map<String, Object> variables = new HashMap<>();
            variables.put("approvalResult", approved ? "approve" : "reject");
            variables.put("comment", comment);
            
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
}