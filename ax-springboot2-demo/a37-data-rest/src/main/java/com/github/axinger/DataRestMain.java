package com.github.axinger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DataRestMain {
    //    https://blog.csdn.net/weixin_51407397/article/details/128177982
    // Spring Data REST 是提供一个灵活和可配置的机制来编写可以通过HTTP公开的简单服务，
    // 简单来说，而且可以省去大部分controller和services的逻辑，
    // 因为Spring Data REST 已经为你都做好了，目前支持JPA、MongoDB、Neo4j、Solr、Cassandra 和 Gemfire
    public static void main(String[] args) {
        SpringApplication.run(DataRestMain.class, args);
    }

}
