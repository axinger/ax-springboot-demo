package com.github.axinger.config;

import com.github.axinger.service.OrgService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("leaveTaskListener")
@Slf4j
public class LeaveTaskListener implements TaskListener {

    @Autowired
    private OrgService orgService;

    @Override
    public void notify(DelegateTask delegateTask) {
        if (!TaskListener.EVENTNAME_CREATE.equals(delegateTask.getEventName())) {
            return;
        }

        String applicant = (String) delegateTask.getVariable("applicant");
        if (applicant == null) {
            throw new RuntimeException("申请人不能为空");
        }

        try {
            String assignee = switch (delegateTask.getTaskDefinitionKey()) {
                case "directLeaderApproval" -> {
                    log.info("分配直属领导审批");
                    yield orgService.getDirectLeader(applicant);
                }
                case "deptLeaderApproval" -> {
                    log.info("分配部门领导审批");
                    yield orgService.getDeptLeader(applicant);
                }
                case "companyLeaderApproval" -> {
                    log.info("分配公司领导审批");
                    yield orgService.getCompanyLeader();
                }
                default -> null;
            };

            if (assignee != null) {
                delegateTask.setAssignee(assignee);
                log.info("任务[{}]分配审批人: {}", delegateTask.getName(), assignee);
            }
        } catch (Exception e) {
            log.error("分配审批人失败", e);
            throw new RuntimeException("分配审批人失败: " + e.getMessage());
        }
    }
}
