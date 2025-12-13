package com.github.axinger.controller;

import com.github.axinger.service.LeaveProcessService;
import com.github.axinger.service.OrgService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    private LeaveProcessService leaveProcessService;

    @Autowired
    private OrgService orgService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private RepositoryService repositoryService;

    // 2. 模拟员工请假
    @GetMapping("/apply")
    public Map<String, Object> applyLeave(@RequestParam String applicant,
                                          @RequestParam int days,
                                          @RequestParam String reason) {

        ProcessInstance instance = leaveProcessService.startLeaveProcess(applicant, days, reason);

        Map<String, Object> map = new HashMap<>();
        map.put("applicant", applicant);
        map.put("days", days);
        map.put("reason", reason);
        map.put("id", instance.getId());
        map.put("isEnded", instance.isEnded());
        map.put("isSuspended", instance.isSuspended());
        map.put("getBusinessKey", instance.getBusinessKey());
        map.put("getBusinessStatus", instance.getBusinessStatus());
        map.put("getDeploymentId", instance.getDeploymentId());
        map.put("getId", instance.getId());
        map.put("getParentId", instance.getParentId());
        map.put("getProcessDefinitionId", instance.getProcessDefinitionId());
        map.put("getProcessDefinitionKey", instance.getProcessDefinitionKey());
        map.put("getProcessDefinitionName", instance.getProcessDefinitionName());
        map.put("getProcessDefinitionVersion", instance.getProcessDefinitionVersion());
        map.put("getProcessVariables", instance.getProcessVariables());
        map.put("getReferenceId", instance.getReferenceId());
        map.put("getRootProcessInstanceId", instance.getRootProcessInstanceId());


        return map;
    }

    @GetMapping("/check")
    public String applyLeave(String processInstanceId, String applicant) {
        // 3. 验证任务分配
        Task task = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .singleResult();

        // 检查流程定义
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .processDefinitionKey("employeeLeave")
                .latestVersion()
                .singleResult();


        // 验证审批人是否正确
        String deptLeader = orgService.getDeptLeader(applicant);


        Assert.isTrue(deptLeader.equals(task.getAssignee()), "");

        return "部门领导审批:" + task.getName();

    }
}
