package com.github.axinger.config;

import org.flowable.task.service.delegate.DelegateTask;
import org.flowable.task.service.delegate.TaskListener;
import org.springframework.stereotype.Component;


@Component
public class LeaveTaskNotificationListener implements TaskListener {

//    @Autowired
//    private EmailService emailService;

    @Override
    public void notify(DelegateTask delegateTask) {
        if (TaskListener.EVENTNAME_CREATE.equals(delegateTask.getEventName())) {
            String assignee = delegateTask.getAssignee();
            String taskName = delegateTask.getName();

//            emailService.sendEmail(
//                assignee,
//                "您有新的审批任务",
//                String.format("请审批【%s】任务", taskName)
//            );
            System.out.println("发送邮件===============" + String.format("请审批【%s】任务", taskName));
        }
    }
}
