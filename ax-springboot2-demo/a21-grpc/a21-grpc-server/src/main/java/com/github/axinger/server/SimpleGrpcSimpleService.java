package com.github.axinger.server;

import com.alibaba.fastjson2.JSON;
import com.github.axinger.api.MyRequest;
import com.github.axinger.api.MyResponse;
import com.github.axinger.api.MyStructDto;
import com.github.axinger.api.SimpleGrpc;
import com.github.axinger.config.GrpcHeaderUtil;
import com.google.protobuf.ListValue;
import com.google.protobuf.StringValue;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import io.grpc.Context;
import io.grpc.Metadata;
import io.grpc.stub.StreamObserver;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import com.github.axinger.api.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/// **
// * GrpcServerService.java中有几处需要注意：
// * <p>
// * 是使用@GrpcService注解，再继承SimpleImplBase，这样就可以借助grpc-server-spring-boot-starter库将oneToOne暴露为gRPC服务；
// * <p>
// * SimpleImplBase是前文中根据maven compile编译 proto文件自动生成的java代码，
// * <p>
// * oneToOne方法中处理完毕业务逻辑后，调用responseObserver.onNext方法填入返回内容；
// * <p>
// * 调用responseObserver.onCompleted方法表示本次gRPC服务完成；
// */
@GrpcService
@Slf4j
public class SimpleGrpcSimpleService extends SimpleGrpc.SimpleImplBase {

    private static final Map<String, StreamObserver<MyResponse>> activeClients = new ConcurrentHashMap<>();

    private static final Metadata.Key<String> CUSTOM_HEADER = Metadata.Key.of("userName", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public void sendMessage(MyRequest request, StreamObserver<MyResponse> responseObserver) {

        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("content-type", GrpcHeaderUtil.getHeader("content-type"));
        headersMap.put("authorization", GrpcHeaderUtil.getHeader("authorization"));
        headersMap.put("clientId", GrpcHeaderUtil.getHeader("client-id"));
        headersMap.put("header-user-name", GrpcHeaderUtil.getHeader("header-user-name"));
        headersMap.put("user-agent", GrpcHeaderUtil.getHeader("user-agent"));

        log.info("服务端获取请求头={}", JSON.toJSONString(headersMap));


//        Context.Key<Metadata> HEADER_KEY =GrpcHeaderUtil.HEADERS_KEY;
//        Context.Key<Metadata> HEADER_KEY = Context.current().key("my-grpc-headers");
        Context.Key<Metadata> HEADER_KEY = Context.key("my-grpc-headers");
        Metadata headers = HEADER_KEY.get();
        if (headers != null) {
            String authorization = headers.get(Metadata.Key.of("authorization", Metadata.ASCII_STRING_MARSHALLER));
            System.out.println("authorization = " + authorization);
        } else {
            System.err.println("headers null============");
        }

        log.info("getUserId={}", request.getUserId());
        MyResponse response = MyResponse.newBuilder()
                .setMessage("SimpleGrpc.SimpleImplBase#sendMessage,返回" + request.getUserId())
                .setResult(1)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sendMessageStream(MyRequest request, StreamObserver<MyResponse> responseObserver) {

        log.info("开始流式处理请求: {}", request.getUserId());

        try {
            // 示例：发送3个流式响应
            for (int i = 1; i <= 3; i++) {
                MyResponse response = MyResponse.newBuilder()
                        .setMessage(request.getUserId() + "###" + i)
                        .setResult(i)
                        .build();
                responseObserver.onNext(response); // 发送单个响应
                Thread.sleep(1000); // 模拟处理延迟
            }
        } catch (Exception e) {
            responseObserver.onError(e); // 异常处理
        } finally {
            responseObserver.onCompleted();
            log.info("流式处理完成");
        }
    }

    @Override
    public void get1(StringValue request, StreamObserver<StringValue> responseObserver) {


        String value = request.getValue();
        System.out.println("get1参数 = " + value);

        Map<String, Object> map = new HashMap<>();
        map.put("name", "jim");
        map.put("age", 10);

        String jsonString = JSON.toJSONString(map);
        StringValue stringValue = StringValue.of(jsonString);
        responseObserver.onNext(stringValue);
        responseObserver.onCompleted();
    }

    @SneakyThrows
    @Override
    public void get2(StringValue request, StreamObserver<MyStructDto> responseObserver) {
        log.info("get2接收客户端数据{}", request);

        // 创建 Struct 并填充数据
        Struct.Builder structBuilder = Struct.newBuilder();

        // 添加一个字符串字段
        structBuilder.putFields("name", Value.newBuilder().setStringValue("Alice").build());

        // 添加一个数字字段
        structBuilder.putFields("age", Value.newBuilder().setNumberValue(30).build());

        // 创建一个嵌套的对象（地址）
        Struct addressStruct = Struct.newBuilder()
                .putFields("street", Value.newBuilder().setStringValue("123 Main St").build())
                .putFields("city", Value.newBuilder().setStringValue("Wonderland").build())
                .build();
        structBuilder.putFields("address", Value.newBuilder().setStructValue(addressStruct).build());

        // 创建一个数组（爱好）
        ListValue hobbiesList = ListValue.newBuilder()
                .addValues(Value.newBuilder().setStringValue("Reading").build())
                .addValues(Value.newBuilder().setStringValue("Hiking").build())
                .build();
        structBuilder.putFields("hobbies", Value.newBuilder().setListValue(hobbiesList).build());

        Map<String, Value> map = new HashMap<>();

        structBuilder.putAllFields(map);

        // 构建最终的 Struct
        Struct details = structBuilder.build();


        // 创建请求对象并设置 details 字段
        MyStructDto dto = MyStructDto.newBuilder()
                .setDetails(details)
                .build();

        // 打印出 JSON 格式的请求内容以验证
//        System.out.println(JsonFormat.printer().print(dto.getDetails()));

        responseObserver.onNext(dto);
        responseObserver.onCompleted();

    }


}
