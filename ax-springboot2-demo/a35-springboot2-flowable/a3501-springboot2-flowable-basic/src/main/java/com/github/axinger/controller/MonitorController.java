package com.github.axinger.controller;

import com.github.axinger.domain.A35UserEntity;
import com.github.axinger.service.FlowableService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/monitor")
public class MonitorController {

    @Autowired
    private FlowableService flowableService;

    /**
     * 获取监控统计数据
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getMonitorStatistics(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 检查用户登录状态
            A35UserEntity user = (A35UserEntity) session.getAttribute("user");
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户未登录");
                return ResponseEntity.status(401).body(response);
            }

            // 获取运行中流程实例数量
            List<ProcessInstance> runningProcessInstances = flowableService.createProcessInstanceQuery().list();
            
            // 获取已完成流程实例数量
            List<HistoricProcessInstance> finishedProcessInstances = 
                flowableService.createHistoricProcessInstanceQuery().finished().list();
            
            // 获取待处理任务数量
            List<Task> pendingTasks = flowableService.createTaskQuery().list();
            
            // 获取流程定义数量
            List<ProcessDefinition> processDefinitions = flowableService.getProcessDefinitions();

            Map<String, Object> data = new HashMap<>();
            data.put("runningProcessCount", runningProcessInstances.size());
            data.put("finishedProcessCount", finishedProcessInstances.size());
            data.put("pendingTaskCount", pendingTasks.size());
            data.put("processDefinitionCount", processDefinitions.size());

            response.put("success", true);
            response.put("data", data);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取监控统计数据失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 获取流程实例状态分布
     */
    @GetMapping("/process-status-distribution")
    public ResponseEntity<Map<String, Object>> getProcessStatusDistribution(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 检查用户登录状态
            A35UserEntity user = (A35UserEntity) session.getAttribute("user");
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户未登录");
                return ResponseEntity.status(401).body(response);
            }

            // 获取运行中流程实例
            List<ProcessInstance> runningProcessInstances = flowableService.createProcessInstanceQuery().list();
            
            // 获取已完成流程实例
            List<HistoricProcessInstance> finishedProcessInstances = 
                flowableService.createHistoricProcessInstanceQuery().finished().list();
            
            // 获取已挂起流程实例
            List<ProcessInstance> suspendedProcessInstances = 
                flowableService.createProcessInstanceQuery().suspended().list();

            Map<String, Integer> distribution = new HashMap<>();
            distribution.put("running", runningProcessInstances.size());
            distribution.put("finished", finishedProcessInstances.size());
            distribution.put("suspended", suspendedProcessInstances.size());

            response.put("success", true);
            response.put("data", distribution);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取流程状态分布失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * 获取任务处理时效统计
     */
    @GetMapping("/task-duration-statistics")
    public ResponseEntity<Map<String, Object>> getTaskDurationStatistics(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // 检查用户登录状态
            A35UserEntity user = (A35UserEntity) session.getAttribute("user");
            if (user == null) {
                response.put("success", false);
                response.put("message", "用户未登录");
                return ResponseEntity.status(401).body(response);
            }

            // 获取已完成任务
            List<?> finishedTasks = flowableService.createHistoricTaskInstanceQuery().finished().list();

            // 这里简化处理，实际项目中可以根据任务定义Key分类统计
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("taskCount", finishedTasks.size());
            // 真实项目中应该计算平均处理时间等指标

            response.put("success", true);
            response.put("data", statistics);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取任务处理时效统计失败: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}