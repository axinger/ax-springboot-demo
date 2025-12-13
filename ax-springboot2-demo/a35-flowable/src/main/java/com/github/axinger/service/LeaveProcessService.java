package com.github.axinger.service;

import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class LeaveProcessService {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 启动请假流程
     */
    public ProcessInstance startLeaveProcess(String applicant, long days, String reason) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("applicant", applicant);
        variables.put("days", days);
        variables.put("reason", reason);

        return runtimeService.startProcessInstanceByKey("employeeLeave", variables);
    }

    /**
     * 审批任务
     */
    public void approveTask(String taskId, boolean approved, String comment) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("approvalResult", approved ? "approve" : "reject");
        variables.put("comment", comment);

        taskService.complete(taskId, variables);
    }

    /**
     * 部署流程定义
     */
    public void deployProcessDefinition() {
        repositoryService.createDeployment()
                .addClasspathResource("processes/employee-leave.bpmn20.xml")
                .name("员工请假流程")
                .deploy();
    }
}
