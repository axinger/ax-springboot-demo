package com.github.axinger.config;

import com.github.axinger.domain.A35UserEntity;
import com.github.axinger.service.A35UserService;
import com.github.axinger.service.OrgService;
import lombok.extern.slf4j.Slf4j;
import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component("expenseTaskAssignment")
@Slf4j
public class ExpenseTaskAssignmentListener implements TaskListener {

    @Autowired
    private OrgService orgService;

    @Autowired
    private A35UserService userService;

    @Override
    public void notify(DelegateTask delegateTask) {
        if (!TaskListener.EVENTNAME_CREATE.equals(delegateTask.getEventName())) {
            return;
        }

        try {
            // 获取流程变量
            String applicant = (String) delegateTask.getVariable("applicant");
            BigDecimal amount = (BigDecimal) delegateTask.getVariable("amount");

            String assignee = null;

            switch (delegateTask.getTaskDefinitionKey()) {
                case "applyTask":
                    log.info("分配申请人提交报销");
                    // 申请人自己提交报销申请
                    assignee = applicant;
                    break;

                case "departmentLeaderReview":
                    log.info("分配部门领导审批");
                    assignee = orgService.getDeptLeader(applicant);
                    break;

                case "financeReview":
                    log.info("分配财务审批");
                    // 查找财务人员
                    A35UserEntity financeUser = userService.lambdaQuery()
                            .eq(A35UserEntity::getPosition, "财务")
                            .last("limit 1")
                            .one();
                    if (financeUser != null) {
                        assignee = financeUser.getName();
                    }
                    break;

                case "viceManagerReview":
                    log.info("分配副总经理审批");
                    // 查找副总经理
                    A35UserEntity viceManager = userService.lambdaQuery()
                            .eq(A35UserEntity::getPosition, "副总经理")
                            .last("limit 1")
                            .one();
                    if (viceManager != null) {
                        assignee = viceManager.getName();
                    }
                    break;

                case "generalManagerReview":
                    log.info("分配总经理审批");
                    // 查找总经理
                    A35UserEntity generalManager = userService.lambdaQuery()
                            .eq(A35UserEntity::getPosition, "总经理")
                            .last("limit 1")
                            .one();
                    if (generalManager != null) {
                        assignee = generalManager.getName();
                    }
                    break;

                default:
                    log.warn("未找到对应的任务定义: {}", delegateTask.getTaskDefinitionKey());
                    break;
            }

            if (assignee != null) {
                delegateTask.setAssignee(assignee);
                log.info("任务[{}]已分配给用户: {}", delegateTask.getName(), assignee);
            } else {
                log.warn("未能为任务[{}]分配审批人", delegateTask.getName());
            }
        } catch (Exception e) {
            log.error("分配审批人失败", e);
            throw new RuntimeException("分配审批人失败: " + e.getMessage());
        }
    }
}
