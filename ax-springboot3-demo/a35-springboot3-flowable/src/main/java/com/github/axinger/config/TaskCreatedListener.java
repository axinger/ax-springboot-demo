//package com.github.axinger.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.flowable.bpmn.model.BpmnModel;
//import org.flowable.bpmn.model.Process;
//import org.flowable.common.engine.api.delegate.event.FlowableEngineEventType;
//import org.flowable.common.engine.api.delegate.event.FlowableEvent;
//import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
//import org.flowable.common.engine.impl.event.FlowableEntityEventImpl;
//import org.flowable.engine.RepositoryService;
//import org.flowable.engine.RuntimeService;
//import org.flowable.engine.TaskService;
//import org.flowable.task.service.impl.persistence.entity.TaskEntity;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//@Slf4j
//public class TaskCreatedListener implements FlowableEventListener {
//
//
//    @Autowired
//    private TaskService taskService;
//
//
//    @Autowired
//    private RuntimeService runtimeService;
//    @Autowired
//    private RepositoryService repositoryService;
//
//    @Override
//    public void onEvent(FlowableEvent event) {
/// /
/// /        if (event.getType().equals(FlowableEngineEventType.PROCESS_STARTED)) {
/// /            // 流程启动事件
/// /            System.out.println("流程已启动");
/// /        } else if (event.getType().equals(FlowableEngineEventType.TASK_COMPLETED)) {
/// /            // 任务完成事件
/// /            System.out.println("任务已完成");
/// /        }
//
//        FlowableEntityEventImpl entityEvent = (FlowableEntityEventImpl) event;
//        TaskEntity taskEntity = (TaskEntity) entityEvent.getEntity();
//        String processInstanceId = taskEntity.getProcessInstanceId();
//        BpmnModel bpmnModel = repositoryService.getBpmnModel(taskEntity.getProcessDefinitionId());
//        List<Process> processes = bpmnModel.getProcesses();
//
//        log.info("监听任务 taskEntity.getId()={}", taskEntity.getId());
//
//
/// /        processes.forEach(process -> process.findFlowElementsOfType(UserTask.class)
/// /                .forEach(userTask -> {
/// /
/// /                    if (StringUtil.equals(userTask.getId(), taskEntity.getTaskDefinitionKey()) && StringUtil.isNotBlank(userTask.getAssignee())) {
/// /                        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
/// /                        if (StringUtil.equals(userTask.getAssignee().toLowerCase(), FlowAssigneeEnum.INITIATOR.getKey())) {
/// /                            taskService.setAssignee(taskEntity.getId(), processInstance.getStartUserId());
/// /                        } else if (StringUtil.equals(userTask.getAssignee().toLowerCase(), FlowAssigneeEnum.SUPERIOR.getKey())) {
/// /                            String startUserId = processInstance.getStartUserId();
/// /                            User startUser = UserCache.getUser(Long.parseLong(startUserId));
/// /                            startUser.getDeptId();
/// /                            // TODO 自行实现获取上级领导用户id
/// /                        }
/// /                    }
/// /                }));
//
//
//        if (event.getType() == FlowableEngineEventType.TASK_CREATED) {
//            // 自定义逻辑，比如设置候选人，记录日志，发送通知等
//
//            TaskEntity task = (TaskEntity) entityEvent.getEntity();
//
//
//            // 修改候选用户和候选组
////            task.getCandidateUsers().clear();
////            task.getCandidateGroups().clear();
//
//            // 添加自定义的候选用户
////            task.addCandidateUser("john");
////            task.addCandidateUser("jane");
////
////            // 添加自定义的候选组
////            task.addCandidateGroup("sales");
////            task.addCandidateGroup("management");
//            String owner = task.getOwner();
//            log.info("监听-任务开始创建，owner={}", owner);
//            log.info("监听-任务开始创建，id={}", task.getId());
////            task.setVariable("currentLeader", "101");
////            // 设置任务审批人
////            task.setAssignee("101");
//        }
//    }
//
//
//    @Override
//    public boolean isFireOnTransactionLifecycleEvent() {
//        return false;
//    }
//
//    @Override
//    public String getOnTransaction() {
//        return "";
//    }
//
//    @Override
//    public boolean isFailOnException() {
//        // 如果监听器失败，是否应使事务回滚
//        return true;
//    }
//
//}
