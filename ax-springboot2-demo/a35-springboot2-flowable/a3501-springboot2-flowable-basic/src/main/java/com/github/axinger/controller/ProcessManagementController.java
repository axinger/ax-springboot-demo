package com.github.axinger.controller;

import com.github.axinger.domain.A35UserEntity;
import com.github.axinger.service.FlowableService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProcessManagementController {

    @Autowired
    private FlowableService flowableService;

    /**
     * 检查用户是否已登录
     */
    private A35UserEntity checkUserLoggedIn(HttpSession session) {
        A35UserEntity user = (A35UserEntity) session.getAttribute("user");
        if (user == null) {
            throw new RuntimeException("用户未登录");
        }
        return user;
    }

    /**
     * 获取流程定义列表
     */
    @GetMapping("/api/management/process-definitions")
    @ResponseBody
    public Map<String, Object> getProcessDefinitions(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            checkUserLoggedIn(session);
            List<ProcessDefinition> processDefinitions = flowableService.getProcessDefinitions();
            response.put("success", true);
            response.put("data", processDefinitions);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取流程定义失败: " + e.getMessage());
        }
        return response;
    }

    /**
     * 获取流程实例列表
     */
    @GetMapping("/api/management/process-instances")
    @ResponseBody
    public Map<String, Object> getProcessInstances(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            checkUserLoggedIn(session);
            List<ProcessInstance> processInstances = flowableService.createProcessInstanceQuery().list();
            response.put("success", true);
            response.put("data", processInstances);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取流程实例失败: " + e.getMessage());
        }
        return response;
    }

    /**
     * 获取任务列表
     */
    @GetMapping("/api/management/tasks")
    @ResponseBody
    public Map<String, Object> getTasks(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            checkUserLoggedIn(session);
            List<Task> tasks = flowableService.createTaskQuery().list();
            response.put("success", true);
            response.put("data", tasks);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取任务失败: " + e.getMessage());
        }
        return response;
    }

    /**
     * 获取已完成的流程历史记录
     */
    @GetMapping("/api/management/history/process-instances")
    @ResponseBody
    public Map<String, Object> getHistoricProcessInstances(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            checkUserLoggedIn(session);
            List<HistoricProcessInstance> historicProcessInstances = 
                flowableService.createHistoricProcessInstanceQuery().finished().list();
            response.put("success", true);
            response.put("data", historicProcessInstances);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取流程历史失败: " + e.getMessage());
        }
        return response;
    }

    /**
     * 获取已完成的任务历史记录
     */
    @GetMapping("/api/management/history/tasks")
    @ResponseBody
    public Map<String, Object> getHistoricTasks(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            checkUserLoggedIn(session);
            List<?> historicTasks = flowableService.createHistoricTaskInstanceQuery().finished().list();
            response.put("success", true);
            response.put("data", historicTasks);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取任务历史失败: " + e.getMessage());
        }
        return response;
    }
}