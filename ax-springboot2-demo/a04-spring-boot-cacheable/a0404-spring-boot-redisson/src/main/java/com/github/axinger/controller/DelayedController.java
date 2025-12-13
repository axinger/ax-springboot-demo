package com.github.axinger.controller;

import com.github.axinger.service.DelayedMessageService;
import com.github.axinger.service.RedissonQueue;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/delayed")
public class DelayedController {

    @Autowired
    RedissonQueue redissonQueue;
    @Autowired
    private DelayedMessageService service;

    @GetMapping("/send")
    public String sendDelayedMessage(@RequestParam String msg,
                                     @RequestParam(defaultValue = "5") long delaySeconds) {
        service.sendMessage(msg, delaySeconds, TimeUnit.SECONDS);
        return "消息已安排，将在 " + delaySeconds + " 秒后处理";
    }

    @Operation(summary = "添加任务", description = "添加任务")
    @GetMapping("/add")
    public ResponseEntity<?> add(@RequestParam(value = "taskId", required = false) String taskId,
                                 @RequestParam(value = "timeout", required = false) Integer timeout) {
        redissonQueue.offer(taskId, timeout);
        return ResponseEntity.ok().body(redissonQueue.delayedQueue());
    }

    @Operation(summary = "移除任务", description = "移除任务")
    @GetMapping("/delete")
    public ResponseEntity<?> remove(String taskId) {
        redissonQueue.remove(taskId);
        return ResponseEntity.ok().body(redissonQueue.delayedQueue());
    }

}
