package com.github.axinger.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.flowable.task.api.history.HistoricTaskInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TaskVO {

    private String id;
    private String day;
    private String studentName;

    static public List<Map<String, Object>> fromHistoricTaskInstance(List<HistoricTaskInstance> historicTasks) {

        List<Map<String, Object>> list = historicTasks.stream().map(task -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", task.getId());
            map.put("processInstanceId", task.getProcessInstanceId());
            map.put("name", task.getName());
            map.put("assignee", task.getAssignee());
            map.put("description", task.getDescription());
            return map;
        }).toList();
        log.info("流程列表={}", list);
        return list;
    }
}
