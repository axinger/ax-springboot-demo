package com.github.axinger.config;

import cn.hutool.crypto.SecureUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.charset.Charset;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class DataConfig {

    private final VectorStore vectorStore;


    @Value("classpath:ops.txt")
    private Resource resource;

   final RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    public void init() {

        //1读取文件
        TextReader textReader = new TextReader(resource);
        textReader.setCharset(Charset.defaultCharset());
        //2文件转换向量,开始分词
        List<Document> list = new TokenTextSplitter().transform(textReader.read());
        //3保存向量
//        vectorStore.add(list);

        //4向量去重, redis setNx命令处理
        String sourceData = (String) textReader.getCustomMetadata().get(TextReader.SOURCE_METADATA);


        String textHash = SecureUtil.md5(sourceData);

        String redisKey = "key:"+textHash;

        /// 6判断是否存入过
        Boolean setIfAbsent = redisTemplate.opsForValue().setIfAbsent(redisKey, "1");
        if (Boolean.TRUE.equals(setIfAbsent)) {
            vectorStore.add(list);
            System.out.println("向量保存成功");
        }else {
            System.err.println("向量已存在");
        }

    }
}
