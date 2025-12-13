package com.github.axinger.controller;

import com.github.axinger.entity.User;
import com.github.axinger.sse.ClientManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

//@Tag(name = "SseController", description = "连接sse")
@RestController
@Slf4j
public class SseController {

    private static final Map<String, SseEmitter> EMITTER_MAP = new ConcurrentHashMap<>();
    @Autowired
    private ClientManager clientManager;

    //    @Operation(summary = "sse页面 ", description = "description注解")
    @RequestMapping(value = "/sse.page")
    public Object ipLogPageInfo1(@RequestParam String id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("sse");
        modelAndView.addObject("id", id);
        return modelAndView;
    }

    //    @Operation(summary = "Connect to SSE")
    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamEvents(@RequestParam String id) {
        return clientManager.getSink(id).asFlux().doFinally(signalType -> clientManager.removeSink(id));
    }

    //    @Operation(summary = "发送sse ", description = "description注解")
    @PostMapping("/send")
    public void sendMessageToClient(@RequestParam String clientId, @RequestBody String message) {
        Sinks.Many<String> sink = clientManager.getSink(clientId);
        sink.tryEmitNext(message);
    }

    @GetMapping("/sse1")
    public ResponseBodyEmitter handle() {
        // 创建一个ResponseBodyEmitter，-1代表不超时
        ResponseBodyEmitter emitter = new ResponseBodyEmitter(-1L);
        // 异步执行耗时操作
        CompletableFuture.runAsync(() -> {
            try {
                // 模拟耗时操作
                for (int i = 0; i < 10000; i++) {
                    System.out.println("bodyEmitter " + i);
                    // 发送数据
                    emitter.send("bodyEmitter " + i + " @ " + new Date() + "\n");
                    Thread.sleep(2000);
                }
                // 完成
                emitter.complete();
            } catch (Exception e) {
                // 发生异常时结束接口
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    @GetMapping("/subSseEmitter/{userId}")
    public SseEmitter sseEmitter(@PathVariable String userId) {
        log.info("sseEmitter: {}", userId);
        SseEmitter emitterTmp = new SseEmitter(-1L);
        EMITTER_MAP.put(userId, emitterTmp);
        CompletableFuture.runAsync(() -> {
            try {
                SseEmitter.SseEventBuilder event = SseEmitter.event()
                        .data("sseEmitter" + userId + " @ " + LocalTime.now())
                        .id(String.valueOf(userId))
                        .name("sseEmitter");
                emitterTmp.send(event);
            } catch (Exception ex) {
                emitterTmp.completeWithError(ex);
            }
        });
        return emitterTmp;
    }

    @GetMapping("/sendSseMsg/{userId}")
    public void sseEmitter(@PathVariable String userId, String msg) throws IOException {
        SseEmitter sseEmitter = EMITTER_MAP.get(userId);
        if (sseEmitter == null) {
            return;
        }
        sseEmitter.send(msg);
    }


    @GetMapping(path = "/em2", produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public ResponseBodyEmitter emitter2() {

        ResponseBodyEmitter emitter = new ResponseBodyEmitter(-1L);
        try {
            emitter.send("Hello World", MediaType.TEXT_PLAIN); // 5
            Thread.sleep(1000);
            emitter.send(User.builder().id(1).build(), MediaType.APPLICATION_JSON);
            Thread.sleep(1000);
            // emitter.send(User.builder().id(2).build(), MediaType.APPLICATION_XML);
            emitter.send(User.builder().id(2).build(), MediaType.APPLICATION_JSON);
            Thread.sleep(1000);
            // emitter.send(User.builder().id(3).build(), new MediaType("application", "another-person"));
            emitter.send(User.builder().id(3).build(), MediaType.APPLICATION_JSON);
            emitter.complete();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return emitter;
    }


    @GetMapping("/sse3")
    public ResponseEntity<StreamingResponseBody> handleRbe() {
        StreamingResponseBody stream = out -> {
            String message = "streamingResponse";
            for (int i = 0; i < 1000; i++) {
                try {
                    out.write(((message + i) + "\r\n").getBytes());
                    out.write("\r\n".getBytes());
                    //调用一次flush就会像前端写入一次数据
                    out.flush();
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(stream);
    }
}
