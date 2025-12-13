package com.github.axinger.config;

import io.grpc.Context;
import io.grpc.Metadata;

public class GrpcHeaderUtil {

    public static final Context.Key<Metadata> HEADERS_KEY = Context.key("my-grpc-headers");


    public static Context storeHeaders(Metadata headers) {
//        Context.current().withValue(HEADERS_KEY, headers).attach();
        return Context.current().withValue(HEADERS_KEY, headers).attach();
    }

    public static Metadata getHeaders() {
        return HEADERS_KEY.get();
    }

    public static String getHeader(String headerName) {
        Metadata headers = getHeaders();
        if (headers != null) {
            return headers.get(Metadata.Key.of(headerName, Metadata.ASCII_STRING_MARSHALLER));
        }
        return null;
    }
}
