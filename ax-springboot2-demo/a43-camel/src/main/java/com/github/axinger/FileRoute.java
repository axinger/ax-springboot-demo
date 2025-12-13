package com.github.axinger;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component  // 声明为Spring组件，由Spring管理
public class FileRoute extends RouteBuilder {  // 继承RouteBuilder，用于定义Camel路由
    @Override
    public void configure() throws Exception {
        // 定义路由：从`input`目录读取文件
        from("file:input?noop=true")
                // 打印日志：当前处理的文件名
                .log("Processing file: ${header.CamelFileName}")
                .process(exchange -> {
                    String fileName = exchange.getIn().getHeader("CamelFileName", String.class);
                    String body = exchange.getIn().getBody(String.class);
                    System.out.println("fileName = " + fileName);
                    System.out.println("body = " + body);
                })
                // 将文件写入`output`目录
                .to("file:output1");
    }


//    常见参数
//    参数	说明	示例
//    noop	是否保留原文件	noop=true（不删除）
//    delete	处理完后删除文件	delete=true
//    recursive	是否递归子目录	recursive=true
//    include	只处理匹配的文件	include=.*\.txt
//    delay	轮询间隔（毫秒）	delay=5000（5秒）
}

/*

关键点解释
from("file:input?noop=true")

file:input：监控 ./input 目录（相对路径），Camel会自动监听该目录下的文件变化。

noop=true：表示文件不会被删除或移动，只是“读取”后保留原文件（适合测试场景）。

如果去掉 noop=true，Camel 默认会移动文件（处理后会删除原文件）。

.log("Processing file: ${header.CamelFileName}")

打印日志，显示当前处理的文件名。

${header.CamelFileName} 是Camel的表达式，获取文件的名称（如 test.txt）。

.to("file:output")

将文件写入 ./output 目录（自动创建目录）。

文件名保持不变（除非手动修改）。

====================================
常见参数
参数	说明	示例
noop	是否保留原文件	noop=true（不删除）
delete	处理完后删除文件	delete=true
recursive	是否递归子目录	recursive=true
include	只处理匹配的文件	include=.*\.txt
delay	轮询间隔（毫秒）	delay=5000（5秒）


==========================================
适用场景
文件备份：自动将文件从A目录复制到B目录。

文件处理：读取文件后，进行数据转换（如CSV→JSON）再存储。

日志收集：监控日志目录，实时处理新日志。

如果需要更复杂的操作（如文件重命名、内容修改），可以在 log() 和 to() 之间添加其他处理器（如 .process(...)）。
 */
