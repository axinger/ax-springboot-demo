package com.github.axinger.controller;

import com.alibaba.fastjson2.JSON;
import com.github.axinger.api.MyRequest;
import com.github.axinger.api.MyResponse;
import com.github.axinger.api.MyStructDto;
import com.github.axinger.api.SimpleGrpc;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.StringValue;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import io.grpc.*;
import io.grpc.stub.MetadataUtils;
import io.grpc.stub.StreamObserver;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.channelfactory.GrpcChannelFactory;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Setter
@Service
@Slf4j
@RestController
/***
 * GrpcClientService类有几处要注意的地方：
 * <p>
 * 用@Service将GrpcClientService注册为spring的普通bean实例；
 * <p>
 * 用@GrpcClient修饰SimpleBlockingStub，这样就可以通过grpc-client-spring-boot-starter库发起gRPC调用，被调用的服务端信息来自名为nacos-grpc服务端配置；
 * <p>
 * SimpleBlockingStub来自前文中根据helloworld.proto生成的java代码；
 * <p>
 * SimpleBlockingStub.oneToOne方法会远程调用nacos-grpc应用的gRPC服务；
 */
public class SimpleGrpcController {


    // 方法名称Grpc.方法名称BlockingStub
    @GrpcClient("a21-grpc-server")
    private SimpleGrpc.SimpleBlockingStub simpleBlockingStub;
    @GrpcClient("a21-grpc-server")
    private SimpleGrpc.SimpleFutureStub simpleFutureStub;
    @GrpcClient("a21-grpc-server")
    private SimpleGrpc.SimpleStub simpleStub;
    @Autowired
    private GrpcChannelFactory channelFactory;

    @GetMapping("/test1")
    public Map<String, Object> sendMessage(final String name) {
        try {
            // 客户端设置请求头
            Metadata metadata = new Metadata();
            metadata.put(Metadata.Key.of("header-user-name", Metadata.ASCII_STRING_MARSHALLER), name);
            ClientInterceptor interceptor = MetadataUtils.newAttachHeadersInterceptor(metadata);

            final MyResponse response = simpleBlockingStub
                    .withInterceptors(interceptor)
                    .sendMessage(MyRequest.newBuilder().setUserId(name).build());


            Map<String, Object> map = new HashMap<>();

            map.put("message", response.getMessage());
            map.put("result", response.getResult());
            return map;
        } catch (Exception e) {
            log.error("sendMessage,e={} ", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/test3")
    public Map<String, Object> sendMessageStream(final String name) {
        try {
            Iterator<MyResponse> messageStream = simpleBlockingStub.sendMessageStream(MyRequest.newBuilder().setUserId(name).build());

            while (messageStream.hasNext()) {
                MyResponse response = messageStream.next();
                System.out.println("messageStream = " + response.getMessage());
                System.out.println("result = " + response.getResult());
            }

            Map<String, Object> map = new HashMap<>();

//            map.put("message", response.getMessage());
//            map.put("result", response.getResult());
            return map;
        } catch (Exception e) {
            log.error("sendMessageStream,e={} ", e.getMessage());
            throw e;
        }
    }

    @GetMapping("/test4")
    public Object send4() {
        ManagedChannel channel = ManagedChannelBuilder
/// /                .forAddress("localhost", 19898)
                .forTarget("a21-grpc-server")
                .usePlaintext()
                .build();
        MyRequest request = MyRequest.newBuilder().setUserId("jim").build();
        SimpleGrpc.SimpleBlockingStub stub = SimpleGrpc.newBlockingStub(channel);
        MyResponse myResponse = stub.sendMessage(request);
        String message = myResponse.getMessage();
        System.out.println("message = " + message);
        return message;
    }

    @GetMapping("/test5")
    public Object test51() {

        Channel channel = channelFactory.createChannel("a21-grpc-server");
        SimpleGrpc.SimpleStub newedStub = SimpleGrpc.newStub(channel);

        newedStub.sendMessage(MyRequest.newBuilder().setUserId("").build(), new StreamObserver<MyResponse>() {

            @Override
            public void onNext(MyResponse myResponse) {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {

            }
        });

        SimpleGrpc.SimpleBlockingStub blockingStub = SimpleGrpc.newBlockingStub(channel);
        final MyResponse response = blockingStub.sendMessage(MyRequest.newBuilder().setUserId("name").build());

        return response.getMessage();
    }

    @SneakyThrows
    public void test4() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "jim");
        String jsonString = JSON.toJSONString(map);
        StringValue hello = StringValue.newBuilder().setValue(jsonString).build();
        StringValue stub1 = simpleBlockingStub.get1(hello);
        String value = stub1.getValue();
        System.out.println(value);
    }


    @SneakyThrows
    public void test5() {
        StringValue hello = StringValue.newBuilder().setValue("hello").build();
        MyStructDto stub2 = simpleBlockingStub.get2(hello);

        Struct details = stub2.getDetails();

        {
            Value name = details.getFieldsOrThrow("name");
            String stringValue = name.getStringValue();
            System.out.println("stringValue = " + stringValue);
        }

        try {
            Value name = details.getFieldsOrThrow("name2");
            String stringValue = name.getStringValue();
            System.out.println("stringValue = " + stringValue);
        } catch (Exception e) {
            log.info("错误={}", Throwables.getRootCause(e).getMessage());
        }

        {

            Value name2 = details.getFieldsOrDefault("name1", Value.newBuilder().setStringValue("没有取到默认值").build());
            String stringValue1 = name2.getStringValue();
            System.out.println("stringValue1 = " + stringValue1);
        }
    }

    //simpleFutureStub 只有普通模式
    @GetMapping("/test21")
    public Map<String, Object> test21(final String name) {
        Map<String, Object> map = new HashMap<>();
        try {
            ListenableFuture<MyResponse> sentMessage = simpleFutureStub.sendMessage(MyRequest.newBuilder().setUserId(name).build());
            // 添加超时和结果获取
            MyResponse response = sentMessage.get(5, TimeUnit.SECONDS); // 5秒超时

            map.put("message", response.getMessage());
            map.put("result", response.getResult());
            return map;
        } catch (Exception e) {
            log.error("FAILED with name={}", name, e); // 记录完整异常堆栈
            map.put("error", "Request failed");
            map.put("message", e.getMessage());
            return map;
        }
    }

    @SneakyThrows
    @GetMapping("/test31")
    public Map<String, Object> test31(final String name) {
        final CountDownLatch latch = new CountDownLatch(1); // 用于等待异步调用完成
        try {
            Map<String, Object> map = new HashMap<>();
            StreamObserver<MyResponse> responseObserver = new StreamObserver<MyResponse>() {
                @Override
                public void onNext(MyResponse response) {
                    // 处理服务器返回的响应
                    System.out.println("Received response: " + response.getMessage());
                    map.put("message", response.getMessage());
                    map.put("result", response.getResult());
                }

                @Override
                public void onError(Throwable t) {
                    // 处理错误
                    System.err.println("Error occurred: " + t.getMessage());
                    t.printStackTrace();
                    latch.countDown();
                }

                @Override
                public void onCompleted() {
                    // RPC调用完成
                    System.out.println("RPC completed");
                    latch.countDown();
                }
            };

            simpleStub.sendMessage(MyRequest.newBuilder().setUserId(name).build(), responseObserver);
            // 等待响应或超时
            if (!latch.await(5, TimeUnit.SECONDS)) {
                map.put("error", "Request timeout");
            }

            return map;
        } catch (Exception e) {
            log.error("FAILED with={} ", e.getMessage());
            throw e;
        }
    }

    @SneakyThrows
    @GetMapping("/test32")
    public Object test32(final String name) {
        final CountDownLatch latch = new CountDownLatch(1); // 用于等待异步调用完成
        try {

            List<Map<String, Object>> list = new ArrayList<>();
            StreamObserver<MyResponse> responseObserver = new StreamObserver<MyResponse>() {
                @Override
                public void onNext(MyResponse response) {
                    // 处理服务器返回的响应
                    System.out.println("Received response: " + response.getMessage());

                    Map<String, Object> map = new HashMap<>();
                    map.put("message", response.getMessage());
                    map.put("result", response.getResult());
                    list.add(map);
                }

                @Override
                public void onError(Throwable t) {
                    // 处理错误
                    System.err.println("Error occurred: " + t.getMessage());
                    t.printStackTrace();
                    latch.countDown();
                }

                @Override
                public void onCompleted() {
                    // RPC调用完成
                    System.out.println("RPC completed");
                    latch.countDown();
                }
            };

            simpleStub.sendMessageStream(MyRequest.newBuilder().setUserId(name).build(), responseObserver);
            // 等待流完成或超时
            if (!latch.await(10, TimeUnit.SECONDS)) {
                log.warn("Stream processing timeout");
            }
            return list;
        } catch (Exception e) {
            log.error("FAILED with={} ", e.getMessage());
            throw e;
        }
    }
}
