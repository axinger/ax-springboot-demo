package com.github.axinger.controller.api;

import com.github.axinger.service.FlowableService;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务管理控制器
 * 展示Flowable任务相关操作
 */
@RestController
@RequestMapping("/api/task")
public class TaskManagementController {

    @Autowired
    private FlowableService flowableService;

    /**
     * 获取用户任务列表 - 指派给用户
     *
     * @param assignee 用户ID
     * @return 任务列表
     */
    @GetMapping("/assignee/{assignee}")
    public List<Task> getTasksByAssignee(@PathVariable String assignee) {
        return flowableService.createTaskQuery()
                .taskAssignee(assignee)
                .list();
    }

    /**
     * 获取用户任务列表 - 候选用户
     *
     * @param candidateUser 候选用户ID
     * @return 任务列表
     */
    @GetMapping("/candidate/user/{candidateUser}")
    public List<Task> getTasksByCandidateUser(@PathVariable String candidateUser) {
        return flowableService.createTaskQuery()
                .taskCandidateUser(candidateUser)
                .list();
    }

    /**
     * 获取组任务列表 - 候选组
     *
     * @param candidateGroup 候选组ID
     * @return 任务列表
     */
    @GetMapping("/candidate/group/{candidateGroup}")
    public List<Task> getTasksByCandidateGroup(@PathVariable String candidateGroup) {
        return flowableService.createTaskQuery()
                .taskCandidateGroup(candidateGroup)
                .list();
    }

    /**
     * 获取所有任务
     *
     * @return 任务列表
     */
    @GetMapping("/all")
    public List<Task> getAllTasks() {
        return flowableService.createTaskQuery().list();
    }

    /**
     * 完成任务
     *
     * @param taskId    任务ID
     * @param variables 任务变量
     * @return 操作结果
     */
    @PostMapping("/{taskId}/complete")
    public Map<String, Object> completeTask(@PathVariable String taskId,
                                            @RequestBody(required = false) Map<String, Object> variables) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (variables != null) {
                flowableService.completeTask(taskId, variables);
            } else {
                flowableService.completeTask(taskId);
            }
            result.put("success", true);
            result.put("message", "任务完成成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 设置任务受理人
     *
     * @param taskId   任务ID
     * @param assignee 受理人
     * @return 操作结果
     */
    @PostMapping("/{taskId}/assignee")
    public Map<String, Object> setAssignee(@PathVariable String taskId,
                                           @RequestParam String assignee) {
        Map<String, Object> result = new HashMap<>();
        try {
            flowableService.setAssignee(taskId, assignee);
            result.put("success", true);
            result.put("message", "任务受理人设置成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 设置任务负责人
     *
     * @param taskId 任务ID
     * @param owner  负责人
     * @return 操作结果
     */
    @PostMapping("/{taskId}/owner")
    public Map<String, Object> setOwner(@PathVariable String taskId,
                                        @RequestParam String owner) {
        Map<String, Object> result = new HashMap<>();
        try {
            flowableService.setOwner(taskId, owner);
            result.put("success", true);
            result.put("message", "任务负责人设置成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 添加候选用户
     *
     * @param taskId 任务ID
     * @param userId 用户ID
     * @return 操作结果
     */
    @PostMapping("/{taskId}/candidate/user/{userId}")
    public Map<String, Object> addCandidateUser(@PathVariable String taskId,
                                                @PathVariable String userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            flowableService.addCandidateUser(taskId, userId);
            result.put("success", true);
            result.put("message", "候选用户添加成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 添加候选组
     *
     * @param taskId  任务ID
     * @param groupId 组ID
     * @return 操作结果
     */
    @PostMapping("/{taskId}/candidate/group/{groupId}")
    public Map<String, Object> addCandidateGroup(@PathVariable String taskId,
                                                 @PathVariable String groupId) {
        Map<String, Object> result = new HashMap<>();
        try {
            flowableService.addCandidateGroup(taskId, groupId);
            result.put("success", true);
            result.put("message", "候选组添加成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 获取任务的标识链接（包括候选人、候选组等）
     *
     * @param taskId 任务ID
     * @return 标识链接列表
     */
    @GetMapping("/{taskId}/identity-links")
    public List<IdentityLink> getTaskIdentityLinks(@PathVariable String taskId) {
        return flowableService.getTaskIdentityLinks(taskId);
    }

    /**
     * 获取任务变量
     *
     * @param taskId 任务ID
     * @return 任务变量
     */
    @GetMapping("/{taskId}/variables")
    public Map<String, Object> getTaskVariables(@PathVariable String taskId) {
        return flowableService.getTaskVariables(taskId);
    }

    /**
     * 设置任务变量
     *
     * @param taskId    任务ID
     * @param variables 变量
     * @return 操作结果
     */
    @PostMapping("/{taskId}/variables")
    public Map<String, Object> setTaskVariables(@PathVariable String taskId,
                                                @RequestBody Map<String, Object> variables) {
        Map<String, Object> result = new HashMap<>();
        try {
            flowableService.setTaskVariables(taskId, variables);
            result.put("success", true);
            result.put("message", "任务变量设置成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 获取任务表单key
     *
     * @param taskId 任务ID
     * @return 表单key
     */
    @GetMapping("/{taskId}/form-key")
    public String getTaskFormKey(@PathVariable String taskId) {
        Task task = flowableService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        return task.getFormKey();
    }

    /**
     * 委派任务
     *
     * @param taskId   任务ID
     * @param assignee 被委派人
     * @return 操作结果
     */
    @PostMapping("/{taskId}/delegate")
    public Map<String, Object> delegateTask(@PathVariable String taskId,
                                            @RequestParam String assignee) {
        Map<String, Object> result = new HashMap<>();
        try {
            flowableService.createTaskQuery()
                    .taskId(taskId)
                    .singleResult();
            result.put("success", true);
            result.put("message", "任务委派成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    /**
     * 解决任务（被委派的任务）
     *
     * @param taskId 任务ID
     * @return 操作结果
     */
    @PostMapping("/{taskId}/resolve")
    public Map<String, Object> resolveTask(@PathVariable String taskId) {
        Map<String, Object> result = new HashMap<>();
        try {
            flowableService.createTaskQuery()
                    .taskId(taskId)
                    .singleResult();
            result.put("success", true);
            result.put("message", "任务解决成功");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
}
