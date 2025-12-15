package com.github.axinger.controller;

import com.github.axinger.model.MyChatRequest;
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
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatController {


    private final ChatModel chatModel;
    private final ChatClient studentChatClient;
    private final StudentTool studentTool;

    @GetMapping("/chat1")
    public ResponseEntity<MyChatResponse> chat1(@RequestParam("message") String message) {
        try {

            String response = studentChatClient.prompt()
                    .user(message)
                    .call()
                    .content();

            return ResponseEntity.ok(new MyChatResponse(response));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new MyChatResponse("处理请求时出错: " + e.getMessage()));
        }
    }

    @GetMapping("/chat")
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

    @GetMapping("/ai/generate")
    public Map<String, String> generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        return Map.of("generation", this.chatModel.call(message));
    }

    @GetMapping("/ai/generateStream")
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return this.chatModel.stream(prompt);
    }

}
