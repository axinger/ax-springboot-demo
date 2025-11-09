package com.github.axinger.config;

import com.github.axinger.hands.MyChannelInterceptor;
import com.github.axinger.hands.MyHandshakeHandler;
import com.github.axinger.hands.MyHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


/**
 * @Author JCccc
 * @Description EnableWebSocketMessageBroker-注解开启STOMP协议来传输基于代理的消息，此时控制器支持使用@MessageMapping
 * @Date 2021/6/30 8:53
 */

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    MyHandshakeInterceptor myHandshakeInterceptor;


    @Autowired
    MyHandshakeHandler myHandshakeHandler;


    /**
     * 开放节点
     *
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 注册两个STOMP的endpoint，分别用于广播和点对点

        /**
         *     SockJS 是 WebSocket 技术的一种模拟。为了应对许多浏览器不支持WebSocket协议的问题，设计了备选SockJs。
         *     开启并使用SockJS后，它会优先选用Websocket协议作为传输协议，
         *     如果浏览器不支持Websocket协议，则会在其他方案中，选择一个较好的协议进行通讯。
         */

        // 广播
        /*
         * 1. 将 /serviceName/stomp/websocketJs路径注册为STOMP的端点，
         *    用户连接了这个端点后就可以进行websocket通讯，支持socketJs
         * 2. setAllowedOrigins("*")表示可以跨域
         * 3. withSockJS()表示支持socktJS访问
         * 4. addInterceptors 添加自定义拦截器，这个拦截器是上一个demo自己定义的获取httpsession的拦截器
         */
        registry.addEndpoint("/publicServer")
                .setHandshakeHandler(myHandshakeHandler)
                .addInterceptors(myHandshakeInterceptor)
                .setAllowedOriginPatterns("*")
                // postman ws://localhost:9908/publicServer/jim/a/websocket
                // 最多2段,必须 websocket结尾
                // 最后可以加上"/websocket"强制使用websocket协议
                .withSockJS()
        ;
//
//
        // 点对点
        registry.addEndpoint("/privateServer")
                .setHandshakeHandler(myHandshakeHandler)
                .addInterceptors(myHandshakeInterceptor)
                .setAllowedOriginPatterns("*")
                .withSockJS();






        /*
         * 看了下源码，它的实现类是WebMvcStompEndpointRegistry ，
         * addEndpoint是添加到WebMvcStompWebSocketEndpointRegistration的集合中，
         * 所以可以添加多个端点
         */

        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();

//        registry.addEndpoint("/greeting").setAllowedOrigins("*").withSockJS();
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // topic用来广播，user用来实现点对点
//        registry.enableSimpleBroker("/topic", "/user");

        // 一对一发送前缀
//        config.setUserDestinationPrefix("/user");
//
//        //后台应用接收浏览器消息端点前缀，这个将直接路由到@MessageMapping上
//        config.setApplicationDestinationPrefixes("/ws");

        // 自定义调度器，用于控制心跳线程
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        // 线程池线程数，心跳连接开线程
        taskScheduler.setPoolSize(1);
        // 线程名前缀
        taskScheduler.setThreadNamePrefix("websocket-heartbeat-thread-");
        // 初始化
        taskScheduler.initialize();

        /*
         * spring 内置broker对象
         * 1. 配置代理域，可以配置多个，此段代码配置代理目的地的前缀为 /topicTest 或者 /userTest
         *    我们就可以在配置的域上向客户端推送消息
         * 2，进行心跳设置，第一值表示server最小能保证发的心跳间隔毫秒数, 第二个值代码server希望client发的心跳间隔毫秒数
         * 3. 可以配置心跳线程调度器 setHeartbeatValue这个不能单独设置，不然不起作用，要配合setTaskScheduler才可以生效
         *    调度器我们可以自己写一个，也可以自己使用默认的调度器 new DefaultManagedTaskScheduler()
         */

        //点对点应配置一个/user消息代理，广播式应配置一个/topic消息代理,群发（mass），单独聊天（alone）
        registry.enableSimpleBroker("/topic", "/user", "/alone", "/queue")
//                .setHeartbeatValue(new long[]{10000, 10000})
//                .setTaskScheduler(taskScheduler)
        ;

        /*
         *  1. 配置一对一消息前缀， 客户端接收一对一消息需要配置的前缀 如“'/user/'+userid + '/message'”，
         *     是客户端订阅一对一消息的地址 stompClient.subscribe js方法调用的地址
         *  2. 使用@SendToUser发送私信的规则不是这个参数设定，在框架内部是用UserDestinationMessageHandler处理，
         *     而不是而不是 AnnotationMethodMessageHandler 或  SimpleBrokerMessageHandler
         *     or StompBrokerRelayMessageHandler，是在@SendToUser的URL前加“user+sessionId"组成
         */

        //点对点使用的订阅前缀（客户端订阅路径上会体现出来），不设置的话，默认也是/user/
        registry.setUserDestinationPrefix("/user");


        // 客户端,发送到服务端, 都要以 /app 开头
        registry.setApplicationDestinationPrefixes("/app");


        /*
         * 自定义路径分割符
         * 注释掉的这段代码添加的分割符为. 分割是类级别的@messageMapping和方法级别的@messageMapping的路径
         * 例如类注解路径为 “topic”,方法注解路径为“hello”，那么客户端JS stompClient.send 方法调用的路径为“/app/topic.hello”
         * 注释掉此段代码后，类注解路径“/topic”,方法注解路径“/hello”,JS调用的路径为“/app/topic/hello”
         */
        //registry.setPathMatcher(new AntPathMatcher("."));
    }

    /**
     * 设置输入消息通道的线程数，默认线程为1，可以自己自定义线程数，最大线程数，线程存活时间
     *
     * @param registration
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {

        /*
         * 配置消息线程池
         * 1. corePoolSize 配置核心线程池，当线程数小于此配置时，不管线程中有无空闲的线程，都会产生新线程处理任务
         * 2. maxPoolSize 配置线程池最大数，当线程池数等于此配置时，不会产生新线程
         * 3. keepAliveSeconds 线程池维护线程所允许的空闲时间，单位秒
         */
        registration.taskExecutor().corePoolSize(10)
                .maxPoolSize(20)
                .keepAliveSeconds(60);
        /*
         * 添加stomp自定义拦截器，可以根据业务做一些处理
         * springframework 4.3.12 之后版本此方法废弃，代替方法 interceptors(ChannelInterceptor... interceptors)
         * 消息拦截器，实现ChannelInterceptor接口
         */
        registration.interceptors(channelInterceptor());
    }

    /**
     * 设置输出消息通道的线程数，默认线程为1，可以自己自定义线程数，最大线程数，线程存活时间
     *
     * @param registration
     */
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(10)
                .maxPoolSize(20)
                .keepAliveSeconds(60);
        //registration.setInterceptors(new WebSocketChannelInterceptor());
    }


    @Bean
    public ChannelInterceptor channelInterceptor() {
        return new MyChannelInterceptor();
    }


}
