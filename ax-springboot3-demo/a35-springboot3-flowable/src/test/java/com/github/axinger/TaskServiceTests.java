package com.github.axinger;

import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class TaskServiceTests {

    @Autowired
    private TaskService taskService;


    @Test
    void taskServiceTest() {

        //此处.taskCandidateGroup("a")的值“a”即是画流程图时辅导员审批节点"分配用户-候选组"中填写的值
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("a").list();

        System.out.println("tasks = " + tasks.size());
    }
}
