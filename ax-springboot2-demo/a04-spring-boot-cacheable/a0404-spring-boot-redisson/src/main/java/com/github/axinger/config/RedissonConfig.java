//package com.github.axinger.config;
//
//import lombok.SneakyThrows;
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.client.codec.Codec;
//import org.redisson.codec.JsonJacksonCodec;
//import org.redisson.config.Config;
//import org.redisson.config.SingleServerConfig;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.File;
//
//@Configuration
//public class RedissonConfig {
//
//    @SneakyThrows
//    @Bean
//    public RedissonClient redissonClient(){
/// /        Config conf = new Config();
/// /        //单节点模式
/// /        SingleServerConfig singleServerConfig = conf.useSingleServer();
/// /        //设置连接地址：redis://127.0.0.1:6379
/// /        singleServerConfig.setAddress("redis://127.0.0.1:6379");
/// /        singleServerConfig.setDatabase(12);
/// /        //设置连接密码
/// /        //使用json序列化方式
/// /        Codec codec = new JsonJacksonCodec();
/// /        conf.setCodec(codec);
//
//        Config config = Config.fromYAML(new File("classpath:redisson.yml"));
//
//        RedissonClient redissonClient = Redisson.create(config);
//        return redissonClient;
//    }
//}
