package com.github.axinger.controller;

import com.github.axinger.model.MyChatResponse;
import com.github.axinger.tool.StudentTool;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SseController {


    private final ChatModel chatModel;
    private final StudentTool studentTool;


    @GetMapping("/sse")
    public Flux<String> chat(@RequestParam("message") String message) {

        ToolCallback[] dateTimeTools = ToolCallbacks.from(studentTool);
        ChatOptions chatOptions = ToolCallingChatOptions.builder()
                .toolCallbacks(dateTimeTools)
                .build();
        Prompt prompt = new Prompt(message, chatOptions);
        return chatModel.stream(prompt)
                .map(ChatResponse::getResult)
                .mapNotNull(result -> result.getOutput().getText());
    }

}
