package com.github.axinger;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class TraceController {

    private final Tracer tracer;

    private final TestService testService;


    @GetMapping("/1")
    public String getTraceId() {
        // 获取当前 trace 的 traceId

        String traceId1 = tracer.currentSpan().start().context().traceId();
        System.out.println("start traceId1 = " + traceId1);


        Optional.ofNullable(tracer).map(Tracer::currentSpan).map(Span::context).ifPresent(val -> {

            String traceId = val.traceId();
            String spanId = val.spanId();
            String parented = val.parentId();
            Boolean sampled = val.sampled();
            System.out.println("sampled = " + sampled);
            System.out.println("parented = " + parented);
            System.out.println("spanId = " + spanId);
            System.out.println("traceId = " + traceId);

        });


        testService.test1();

        return tracer.currentSpan().context().traceId();
    }
}
