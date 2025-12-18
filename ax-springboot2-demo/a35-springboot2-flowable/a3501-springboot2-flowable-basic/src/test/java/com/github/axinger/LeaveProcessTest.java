package com.github.axinger;

import com.github.axinger.service.LeaveProcessService;
import com.github.axinger.service.OrgService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class LeaveProcessTest {

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

    @Test
    void test_1部署流程() {
        // 1. 部署流程
        leaveProcessService.deployProcessDefinition();
    }

    @Test
    @Transactional
    public void testLeaveProcessWithVerification() {
        // 1. 部署流程
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("processes/employee-leave.bpmn20.xml")
                .deploy();
        assertNotNull(deployment);

        // 2. 启动流程
        String applicant = "e001";
        long days = 2; // 测试部门领导审批分支
        String reason = "年假";

        Map<String, Object> variables = new HashMap<>();
        variables.put("applicant", applicant);
        variables.put("days", days);
        variables.put("reason", reason);

        ProcessInstance instance = runtimeService.startProcessInstanceByKey("employeeLeave", variables);
        assertNotNull(instance);
        System.out.println("instance = " + instance.getId());

        // 3. 验证流程变量
        Map<String, Object> processVariables = runtimeService.getVariables(instance.getId());
        assertEquals(days, processVariables.get("days"));

        // 4. 查询任务
        Task task = taskService.createTaskQuery()
                .processInstanceId(instance.getId())
                .singleResult();
        assertNotNull(task, "未找到任务，请检查流程定义和监听器");

        // 5. 验证任务分配
        String assignee = task.getAssignee();
        assertNotNull(assignee, "任务未分配审批人");
        System.out.println("任务ID: " + task.getId() + ", 审批人: " + assignee);

        // 6. 验证是否正确进入部门领导审批
        assertEquals("部门领导审批", task.getName());
    }
}
