package com.github.axinger.controller;

import com.github.axinger.util.DateTimeTools;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class ChatController {


    @Resource
    private ChatClient chatClient;


    @GetMapping("/test1")
    public Flux<String> test1(@RequestParam(name = "msg", defaultValue = "你是谁现在几点？") String msg) {

        return chatClient.prompt("获取北京时间")
                .user(msg)
                .stream()
                .content();

    }

    @GetMapping("/test2")
    public Flux<String> test2(@RequestParam(name = "msg", defaultValue = "你是谁现在几点？") String msg) {

        //1.工具注册到工具集合
        ToolCallback[] toolCallbacks = ToolCallbacks.from(new DateTimeTools());
        //2.工具集合配置到选项
        ToolCallingChatOptions toolCallingChatOptions = ToolCallingChatOptions.builder().toolCallbacks(toolCallbacks).build();
        //3.构建提示词
        Prompt prompt = new Prompt(msg, toolCallingChatOptions);
        //4.调用
        return chatClient.prompt(prompt)
                .user(msg)
                .stream()
                .content();
    }

    @GetMapping("/test3")
    public Flux<String> test3(@RequestParam(name = "msg", defaultValue = "你是谁现在几点？") String msg) {
        return chatClient.prompt(msg)
                .tools(new DateTimeTools())
                .stream()
                .content();
    }

}
