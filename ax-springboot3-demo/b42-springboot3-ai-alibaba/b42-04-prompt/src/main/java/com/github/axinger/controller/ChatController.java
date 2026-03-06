package com.github.axinger.controller;

import com.github.axinger.model.Story;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@RestController
public class ChatController {

    @Resource
    @Qualifier("dashScopeChatModel")
    private ChatModel dashScopeChatModel;

    @Resource
    @Qualifier("dashScopeChatClient")
    private ChatClient dashScopeChatClient;
    @Value("classpath:/prompt/123.txt")
    private org.springframework.core.io.Resource resource;

    /// 样板代码
    @GetMapping("/test1")
    public ChatResponse test1(@RequestParam(name = "msg", defaultValue = "你是谁?") String msg) {
        SystemMessage systemMessage = new SystemMessage("你是一个故事助手,每个故事控制在300字以内");
        UserMessage userMessage = new UserMessage(msg);
        Prompt prompt = new Prompt(systemMessage, userMessage);
        ChatResponse call = dashScopeChatModel.call(prompt);
        System.out.println("call = " + call.getResults());
        return call;
    }

    @GetMapping("/test11")
    public Flux<String> test11(@RequestParam(name = "msg", defaultValue = "你是谁?") String msg) {
        SystemMessage systemMessage = new SystemMessage("你是一个故事助手,每个故事控制在300字以内,以html格式返回");
        UserMessage userMessage = new UserMessage(msg);
        Prompt prompt = new Prompt(systemMessage, userMessage);
        return dashScopeChatModel.stream(prompt).mapNotNull(chatResponse -> chatResponse.getResults().getFirst().getOutput().getText());
    }

    @GetMapping("/test21")
    public Flux<String> test21(@RequestParam(name = "msg", defaultValue = "你是谁?") String msg) {
        return dashScopeChatClient.prompt()
                /// AI能力边界
                .system("你是一个故事助手,每个故事控制在300字以内,以html格式返回")
                /// 用户的提问
                .user(msg)
                .stream()
                .content();
    }

    @GetMapping("/test31")
    public String test31(@RequestParam(name = "msg", defaultValue = "葫芦娃") String msg) {
        AssistantMessage assistantMessage = dashScopeChatClient.prompt()
                /// AI能力边界
                .system("你是一个故事助手,每个故事控制在300字以内")
                /// 用户的提问
                .user(msg)
                .call()
                .chatResponse()
                .getResult()
                .getOutput();
        return assistantMessage.getText();
    }

    @GetMapping("/test41")
    public String test41(@RequestParam(name = "city", defaultValue = "南京") String city) {
        String answer = dashScopeChatClient.prompt()
                /// AI能力边界
                .system("你是一个天气助手")
                /// 用户的提问
                .user(city + "未来3天天天气情况如何?")
                .call()
                .chatResponse()
                .getResult()
                .getOutput()
                .getText();

        List<ToolResponseMessage.ToolResponse> responseList = List.of(new ToolResponseMessage.ToolResponse("1", "获取天气", city));
        ToolResponseMessage toolResponseMessage = ToolResponseMessage.builder()
                .responses(responseList)
                .build();
        String responseMessageText = toolResponseMessage.getText();
        return answer + responseMessageText;
    }

    @GetMapping("/test51")
    public Flux<String> test51() {

        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template("""
                        讲一个关于{topic}的故事,
                        并以{output_format}格式输出,
                        字数控制在{wordCount}左右
                        """)
                .build();
        Prompt prompt = promptTemplate.create(Map.of(
                "topic", "葫芦娃",
                "output_format", "html",
                "wordCount", 300
        ));

        return dashScopeChatClient.prompt(prompt).stream().content();
    }

    @GetMapping("/test52")
    public Flux<String> test52() {

        PromptTemplate promptTemplate = PromptTemplate.builder()
                .resource(resource)
                .build();
        Prompt prompt = promptTemplate.create(Map.of(
                "topic", "葫芦娃",
                "output_format", "html",
                "wordCount", 300
        ));

        return dashScopeChatClient.prompt(prompt).stream().content();
    }


    @GetMapping("/test53")
    public String test53() {


        SystemPromptTemplate systemPromptTemplate = SystemPromptTemplate.builder()
                .template("你是一个{sysTopic}")
                .build();
        Message message1 = systemPromptTemplate.createMessage(Map.of(
                "sysTopic", "故事助手",
                "output_format", "html",
                "wordCount", 300
        ));

        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template("""
                        讲一个关于{topic}的故事,
                        并以{output_format}格式输出,
                        字数控制在{wordCount}左右
                        """)
                .build();

        Message message2 = promptTemplate.createMessage(Map.of(
                "topic", "葫芦娃",
                "output_format", "html",
                "wordCount", 300
        ));

        /// 多个提示词
        Prompt prompt = new Prompt(List.of(message1, message2));
        return dashScopeChatClient.prompt(prompt).call().content();
    }

    /*
    {
id: "huluwa-story-001",
topic: "葫芦娃",
outputFormat: "html",
wordCount: 298
}
     */
    @GetMapping("/test61")
    public Story test61() {
        Story entity = dashScopeChatClient.prompt()
                .user(new Consumer<ChatClient.PromptUserSpec>() {
                    @Override
                    public void accept(ChatClient.PromptUserSpec promptUserSpec) {

                        promptUserSpec.text("""
                                                讲一个关于{topic}的故事,
                                                并以{output_format}格式输出,
                                                字数控制在{wordCount}左右
                                        """)
                                .params(Map.of(
                                        "topic", "葫芦娃",
                                        "output_format", "html",
                                        "wordCount", 300
                                ));
                    }
                }).call().entity(Story.class);

        return entity;
    }

    /// 对话记忆,持久化
}
