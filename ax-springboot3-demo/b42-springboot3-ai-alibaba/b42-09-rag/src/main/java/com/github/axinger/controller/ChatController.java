package com.github.axinger.controller;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;
import com.alibaba.cloud.ai.dashscope.spec.DashScopeModel;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingOptions;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.image.ImageModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {


    private final ImageModel imageModel;
    @Resource(name = "dashScopeChatModel")
    private ChatModel chatModel;
    @Resource(name = "qwenChatModel")
    private ChatModel qwenChatModel;

    private final VectorStore vectorStore;

    @Resource
    private EmbeddingModel embeddingModel;

    @GetMapping("/test1")
    public String test1(@RequestParam(name = "msg", defaultValue = "你是谁?") String msg) {
        return chatModel.call(msg);
    }

    @GetMapping("/test2")
    public Flux<String> test2(@RequestParam(name = "msg", defaultValue = "你是谁?") String msg) {
        return chatModel.stream(msg);
    }

    @GetMapping("/test3")
    public String test3(@RequestParam(name = "msg", defaultValue = "你是谁?") String msg) {
        return qwenChatModel.call(msg);
    }


    @GetMapping("/test41")
    public EmbeddingResponse test41(@RequestParam(name = "topic", defaultValue = "java") String topic) {
        EmbeddingOptions options = DashScopeEmbeddingOptions.builder().model(DashScopeModel.EmbeddingModel.EMBEDDING_V3.getValue()).build();
        EmbeddingResponse response = embeddingModel.call(new EmbeddingRequest(List.of(topic), options));
//        EmbeddingResponse response = embeddingModel.call(new EmbeddingRequest(List.of(topic), null));
        String string = ArrayUtil.toString(response.getResult().getOutput());
        System.out.println("string = " + string);
        return response;
    }

    /// 文本向量化,存入数据库
    /// 发生了什么：当你调用 vectorStore.add(list) 时，框架底层会自动调用配置好的 embeddingModel (即 DashScope Embedding V3)。
    @GetMapping("/test42")
    public void test42() {

        List<Document> list = List.of(
                new Document("i study LLM"),
                new Document("i love java"));
        vectorStore.add(list);
    }

    /// 从数据中查找
    @GetMapping("/test43")
    public Object test43(@RequestParam(name = "topic", defaultValue = "java") String topic) {

        SearchRequest request = SearchRequest.builder()
                .query(topic)
                .topK(2)
                .build();
        List<Document> documents = vectorStore.similaritySearch(request);
        System.out.println("documents = " + documents);
        return documents;
    }
}
