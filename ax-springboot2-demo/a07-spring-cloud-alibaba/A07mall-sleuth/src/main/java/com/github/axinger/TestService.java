package com.github.axinger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TestService {

    @Autowired
    private Tracer tracer;

    public void test1() {


        Optional.ofNullable(tracer).map(Tracer::currentSpan).map(Span::context).ifPresent(val -> {
            System.out.println("TestService======================");
            String traceId1 = tracer.currentSpan().start().context().traceId();
            System.out.println("start traceId1 = " + traceId1);
            String traceId = val.traceId();
            String spanId = val.spanId();
            String parented = val.parentId();
            Boolean sampled = val.sampled();
            System.out.println("sampled = " + sampled);
            System.out.println("parented = " + parented);
            System.out.println("spanId = " + spanId);
            System.out.println("traceId = " + traceId);
        });

    }
}
