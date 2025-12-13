package com.github.axinger;


//import org.apache.camel.spi.annotations.Component;

import org.springframework.stereotype.Component;

@Component("dataProcessor")
public class DataProcessor {
    public String process(String data) {
        return "返回数据===: " + data.toUpperCase();
    }
}
