package com.github.axinger.controller.api;

import com.github.axinger.domain.A35UserEntity;
import com.github.axinger.service.FlowableService;
import com.github.axinger.service.LeaveProcessService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/leave-view")
public class LeaveViewController {

    @Autowired
    private LeaveProcessService leaveProcessService;

    @Autowired
    private FlowableService flowableService;

    /**
     * 提交请假申请
     */
    @PostMapping("/apply")
    public ResponseEntity<Map<String, Object>> applyLeave(
            @RequestParam int days,
            @RequestParam String reason,
            HttpSession session) {

        A35UserEntity currentUser = (A35UserEntity) session.getAttribute("user");
        Map<String, Object> response = new HashMap<>();

        if (currentUser == null) {
            response.put("success", false);
            response.put("message", "用户未登录");
            return ResponseEntity.status(401).body(response);
        }

        try {
            String userName = currentUser.getName();
            Map<String, Object> variables = new HashMap<>();
            variables.put("applicant", userName);
            variables.put("days", days);
            variables.put("reason", reason);

            // 启动请假流程
            ProcessInstance processInstance = leaveProcessService.startLeaveProcess(currentUser.getName(), days, reason);

            response.put("success", true);
            response.put("message", "请假申请提交成功");
            response.put("processInstanceId", processInstance.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "提交请假申请失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 审批请假申请
     */
    @PostMapping("/approve/{taskId}")
    public ResponseEntity<Map<String, Object>> approveLeave(
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
