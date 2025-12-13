//package com.github.axinger;
//
//
//import com.github.axinger.api.MyRequest;
//import com.github.axinger.api.MyResponse;
//import com.github.axinger.api.SimpleGrpc;
//import com.github.axinger.service.GrpcClientService;
//import io.grpc.ManagedChannel;
//import io.grpc.ManagedChannelBuilder;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Map;
//
//@SpringBootTest
//class A21GrpcClientApplicationTest {
//
//
//    @Autowired
//    private GrpcClientService service;
//
//    @Test
//    public void test1() {
//        Map<String, Object> map = service.test1("客户端kele连接");
//        System.out.println("map = " + map);
//    }
//
//    @Test
//    public void test2() {
//        ManagedChannel channel = ManagedChannelBuilder
//                .forAddress("localhost", 19898)
//                .usePlaintext()
//                .build();
//        MyRequest request = MyRequest.newBuilder().setName("kele的访问").build();
//        SimpleGrpc.SimpleBlockingStub stub = SimpleGrpc.newBlockingStub(channel);
//        MyResponse myResponse = stub.oneToOne(request);
//
//        System.out.println("myResponse = " + myResponse.getMessage());
//    }
//
//
//    @Test
//    public void test3() {
//        Map<String, Object> map = service.test3();
//        System.out.println("map = " + map);
//    }
//
//    @Test
//    public void test4() {
//        service.test4();
//    }
//
//    @Test
//    public void test5() {
//        service.test5();
//    }
//
//}
