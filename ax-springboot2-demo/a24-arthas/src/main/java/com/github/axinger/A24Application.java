package com.github.axinger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Arthas 演示应用
 * 
 * Arthas 是 Alibaba 开源的 Java 诊断工具，可以在线排查问题、监控 JVM 状态、分析性能瓶颈等。
 * 
 * 启动后可以通过以下方式访问 Arthas:
 * 1. Telnet: telnet 127.0.0.1 3658
 * 2. Web Console: http://127.0.0.1:8563
 * 
 * 常用命令:
 * - dashboard: 查看系统实时数据面板
 * - thread: 查看线程信息
 * - jad: 反编译指定类
 * - watch: 观察方法执行时的参数、返回值、异常等信息
 * - trace: 追踪方法调用路径和耗时
 * - monitor: 监控方法的调用情况
 * - stack: 查看方法调用栈
 * - tt: 时空隧道，记录方法调用的详细信息
 */
@Slf4j
@SpringBootApplication
public class A24Application {

    public static void main(String[] args) {
        SpringApplication.run(A24Application.class, args);
        log.info("Arthas 演示应用已启动！");
        log.info("访问 Arthas Web Console: http://127.0.0.1:8563");
        log.info("或通过 Telnet 连接: telnet 127.0.0.1 3658");
        log.info("API 文档地址: http://127.0.0.1:15024/api/demo/system-info");
    }

}
