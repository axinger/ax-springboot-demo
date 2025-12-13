package com.github.axinger;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class BeanRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("direct:processData")
//            .bean("dataProcessor", "process")
                .bean(DataProcessor.class, "process")
                .to("log:processedData");
    }
}

