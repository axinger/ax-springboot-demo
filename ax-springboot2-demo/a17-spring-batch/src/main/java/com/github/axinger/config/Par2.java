package com.github.axinger.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Par2 implements Partitioner {
    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {

        Map<String, ExecutionContext> result = new HashMap<>();
        Integer initialLastId = 0; // 初始值为 0，从头开始

        for (int i = 0; i < gridSize; i++) {
            ExecutionContext context = new ExecutionContext();
            context.putInt("lastId", initialLastId);
            context.putInt("pageSize", 50); // 每页大小
            result.put("partition_" + i, context);
        }

        return result;
    }
}
