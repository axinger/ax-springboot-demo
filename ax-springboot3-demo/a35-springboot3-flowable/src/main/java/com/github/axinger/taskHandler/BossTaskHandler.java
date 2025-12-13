package com.github.axinger.taskHandler;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

public class BossTaskHandler implements TaskListener {
    @Override
    public void notify(DelegateTask delegateTask) {
//        EVENTNAME_CREATE;
        delegateTask.setAssignee("老板");
    }
}
