package com.github.axinger;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class RestRoute extends RouteBuilder {
    @Override
    public void configure() {
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json);

        rest("/api")
                .get("/hello")
                .to("direct:hello");

        from("direct:hello")
                .transform().constant("Hello from Camel!");
    }
}
