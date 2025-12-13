package com.github.axinger.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;


@Configuration
@EnableCaching
public class RedisConfig {


    /**
     * 自定义key规则
     *
     * @return KeyGenerator
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            sb.append(target.getClass().getName());
            sb.append(method.getName());
            for (Object obj : params) {
                sb.append(obj.toString());
            }
            return sb.toString();
        };
    }


    /**
     * 设置RedisTemplate规则
     *
     * @return RedisTemplate
     */
    @Bean
    @Primary
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        // 序列号key value
        RedisSerializer<String> key = RedisSerializer.string();
        RedisSerializer<Object> value = MyRedisSerializer.jackson2JsonRedisSerializer();
//        RedisSerializer<Object> value = jackson2JsonRedisSerializer();

        redisTemplate.setKeySerializer(key);
        redisTemplate.setValueSerializer(value);
        // hash
        redisTemplate.setHashKeySerializer(key);
        redisTemplate.setHashValueSerializer(value);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean({"myJsonRedisTemplate"})
    public RedisTemplate<?, ?> myJsonRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        // 序列号key value
        RedisSerializer<String> key = RedisSerializer.string();
        /// 含有 "@class
        RedisSerializer<Object> value = RedisSerializer.json();

        redisTemplate.setKeySerializer(key);
        redisTemplate.setValueSerializer(value);
        // hash
        redisTemplate.setHashKeySerializer(key);
        redisTemplate.setHashValueSerializer(value);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean({"myStringRedisTemplate"})
    public RedisTemplate<String, String> stringRedisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        // 全部使用 String 序列化器
        // 序列号key value
        RedisSerializer<String> key = RedisSerializer.string();

        template.setKeySerializer(key);
        template.setValueSerializer(key);
        // hash
        template.setHashKeySerializer(key);
        template.setHashValueSerializer(key);
        template.afterPropertiesSet();
        return template;
    }

    @Bean(name = "bytesRedisTemplate")
    public RedisTemplate<String, byte[]> bytesRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        // 设置key和value的序列化规则
        RedisSerializer<String> key = RedisSerializer.string();
        RedisSerializer<byte[]> value = RedisSerializer.byteArray();

        redisTemplate.setKeySerializer(key);
        redisTemplate.setValueSerializer(value);
        // hash
        redisTemplate.setHashKeySerializer(key);
        redisTemplate.setHashValueSerializer(value);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 设置CacheManager缓存规则
     *
     * @return CacheManager
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        // 配置序列化（解决乱码的问题）,
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()))
                .disableCachingNullValues()
                // 创建默认缓存配置对象、 将@Cacheable缓存key值时默认会给value或cacheNames后加上双冒号 改为 单冒号
                .computePrefixWith(name -> name + ":");
        return RedisCacheManager.builder(factory).cacheDefaults(config).build();
    }


    @Bean
    public RedisSerializer<String> keySerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    public RedisSerializer<Object> jackson2JsonRedisSerializer() {

//        ObjectMapper objectMapper = new CommonObjectMapper(jsonProperties);
//
//        ObjectMapper objectMapper = ObjectMapperFactory.factory(jsonProperties);
//
//
////        // 将当前对象的数据类型也存入序列化的结果字符串中，以便反序列化
//        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
//
//        return new GenericJackson2JsonRedisSerializer(objectMapper);

//        return new FastJsonRedisSerializer<>(Object.class);

//        return new GenericToStringSerializer<>(Object.class);

        // 使用 Jackson2JsonRedisSerializer 序列化值
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        // 解决 LocalDateTime 序列化问题
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule()); // 注册 Java 8 时间模块
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 禁用时间戳格式
        serializer.setObjectMapper(om);
        return serializer;

    }


    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory factory) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        return container;
    }

    @Bean
    public StreamMessageListenerContainer<String, ObjectRecord<String, String>> streamMessageListenerContainer(RedisConnectionFactory connectionFactory) {

        // 用于配置消息监听容器的选项。在这个方法中，通过设置不同的选项，如轮询超时时间和消息的目标类型，可以对消息监听容器进行个性化的配置。
        StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> options =
                StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                        .builder()
                        // 设置了轮询超时的时间为100毫秒。这意味着当没有新的消息时，容器将每隔100毫秒进行一次轮询。
                        .pollTimeout(Duration.ofMillis(100))
                        // 指定了消息的目标类型为 String。这意味着容器会将接收到的消息转换为 String 类型，以便在后续的处理中使用。
                        .targetType(String.class)
                        .build();

        // 创建一个可用于监听Redis流的消息监听容器。

        return StreamMessageListenerContainer.create(connectionFactory, options);
    }


}
