package com.github.axinger.config;

import com.axing.common.json.bean.JsonProperties;
import com.axing.common.json.model.ObjectMapperFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;


@Configuration
@EnableCaching
public class RedisConfig {

    @Autowired
    private JsonProperties jsonProperties;

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
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);

        // 序列号key value
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(this.valueSerializer());

        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(this.valueSerializer());

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
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(this.keySerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(this.valueSerializer()))
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
    public RedisSerializer<Object> valueSerializer() {

//        ObjectMapper objectMapper = new CommonObjectMapper(jsonProperties);

        ObjectMapper objectMapper = ObjectMapperFactory.factory(jsonProperties);


//        // 将当前对象的数据类型也存入序列化的结果字符串中，以便反序列化
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);

        return new GenericJackson2JsonRedisSerializer(objectMapper);

//        return new FastJsonRedisSerializer<>(Object.class);

//        return new GenericFastJsonRedisSerializer();

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
        StreamMessageListenerContainer<String, ObjectRecord<String, String>> listenerContainer =
                StreamMessageListenerContainer.create(connectionFactory, options);

        return listenerContainer;
    }

}
