package com.github.axinger.config;

import io.grpc.*;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

@Slf4j
@GrpcGlobalServerInterceptor
public class HeaderServerInterceptor implements ServerInterceptor {

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        for (String key : headers.keys()) {
            System.out.println("请求头参数:" + key + " = " + headers.get(Metadata.Key.of(key, Metadata.ASCII_STRING_MARSHALLER)));
        }

        /// 必须用返回值覆盖，否则无法获取header
        Context context = GrpcHeaderUtil.storeHeaders(headers);
        return Contexts.interceptCall(context, call, headers, next);
    }
}
