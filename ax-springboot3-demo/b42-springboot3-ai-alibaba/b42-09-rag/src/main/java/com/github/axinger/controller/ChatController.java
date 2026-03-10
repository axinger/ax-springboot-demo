package com.github.axinger.controller;

import cn.hutool.core.util.ArrayUtil;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;
import com.alibaba.cloud.ai.dashscope.spec.DashScopeModel;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingOptions;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
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


    @Resource
    private ChatClient chatClient;

    private final VectorStore vectorStore;

    @Resource
    private EmbeddingModel embeddingModel;


    @GetMapping("/test1")
    public Flux<String> test1(@RequestParam(name = "msg", defaultValue = "0000") String msg) {

        String sysTemplate = """
                你是一个运维工程师,按照给出的编码给出对应的故障解释,否则回复我找不到信息
                """;

        VectorStoreDocumentRetriever documentRetriever = VectorStoreDocumentRetriever.builder().vectorStore(vectorStore).build();

        RetrievalAugmentationAdvisor advisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                .build();

        return chatClient.prompt().system(sysTemplate)
                .user(msg)
                .advisors(advisor)
                .stream()
                .content();

    }

}
