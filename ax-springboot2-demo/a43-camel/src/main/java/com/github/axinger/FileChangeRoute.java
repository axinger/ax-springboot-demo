package com.github.axinger;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

//    事件类型 (CamelFileEventType)	说明
//    CREATE	文件创建
//    DELETE	文件删除
//    MODIFY	文件修改
@Component
public class FileChangeRoute extends RouteBuilder {  // 去掉 abstract
    @Override
    public void configure() throws Exception {
//        from("file-watch:input?events=CREATE,MODIFY,DELETE")
//                .filter(header("CamelFileName").regex("^.*(?<!~)$")) // 排除以 ~ 结尾的文件
//                .log("File event: ${header.CamelFileEventType} on file ${header.CamelFileName}")
//                .to("file:output2");


        from("file-watch:input?events=CREATE,MODIFY,DELETE")
                .delay(100)  // 延迟 100ms，确保文件完全写入
                .filter(exchange -> {
                    String fileName = exchange.getIn().getHeader("CamelFileName", String.class);
                    return fileName != null && !fileName.endsWith("~");  // 过滤临时文件
                })
                .process(exchange -> {
                    String fileName = exchange.getIn().getHeader("CamelFileName", String.class);
                    String body = exchange.getIn().getBody(String.class);
                    System.out.println("监听文件改变===================");
                    System.out.println("fileName = " + fileName);
                    System.out.println("body = " + body);
                })
                .log("File event: ${header.CamelFileEventType} on file ${header.CamelFileName}")
                .to("file:output2");
    }
}
