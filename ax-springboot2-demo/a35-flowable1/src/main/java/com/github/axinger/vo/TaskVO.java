package com.github.axinger.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.history.HistoricTaskInstance;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TaskVO {

    private String id;
    private String processInstanceId;
    private String name;
    private String assignee;
    private String description;
    private Map<String, Object> variablesLocal;

    private String parentTaskId;
    private Date createTime;
    private String owner;
    private String delegationState;
    private String taskDefinitionKey;
    private String processDefinitionId;
    private Map<String, Object> taskLocalVariables;
    private Map<String, Object> processVariables;
    private Map<String, Object> caseVariables;
    private Date claimTime;


    static public List<TaskVO> fromHistoricTaskInstance(List<HistoricTaskInstance> historicTasks) {
        return fromTaskInfo(historicTasks);
    }

    static public List<TaskVO> fromTaskInfo(List<? extends TaskInfo> infos) {
        return infos.stream().map(task -> TaskVO.builder()
                .id(task.getId())
                .processInstanceId(task.getProcessInstanceId())
                .name(task.getName())
                .assignee(task.getAssignee())
                .description(task.getDescription())
                .variablesLocal(task.getTaskLocalVariables())
                .parentTaskId(task.getParentTaskId())
                .createTime(task.getCreateTime())
                .owner(task.getOwner())
//                .delegationState(task.getDelegationState())
                .taskDefinitionKey(task.getTaskDefinitionKey())
                .processDefinitionId(task.getProcessDefinitionId())
                .taskLocalVariables(task.getTaskLocalVariables())
                .processVariables(task.getProcessVariables())
                .caseVariables(task.getCaseVariables())
                .claimTime(task.getClaimTime())
                .build()).toList();
    }

    static public List<TaskVO> fromTask(List<? extends Task> infos) {
        return infos.stream().map(task -> TaskVO.builder()
                .id(task.getId())
                .processInstanceId(task.getProcessInstanceId())
                .name(task.getName())
                .assignee(task.getAssignee())
                .description(task.getDescription())
                .variablesLocal(task.getTaskLocalVariables())
                .parentTaskId(task.getParentTaskId())
                .createTime(task.getCreateTime())
                .owner(task.getOwner())
//                .delegationState(task.getDelegationState())
                .taskDefinitionKey(task.getTaskDefinitionKey())
                .processDefinitionId(task.getProcessDefinitionId())
                .taskLocalVariables(task.getTaskLocalVariables())
                .processVariables(task.getProcessVariables())
                .caseVariables(task.getCaseVariables())
                .claimTime(task.getClaimTime())
                .build()).toList();
    }
}
