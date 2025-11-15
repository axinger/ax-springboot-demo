package com.github.axinger.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

@Component
@Order(value = 1)
@Slf4j
public class Start1 {

    @Autowired
    private InetUtils inetUtils;
    //
    //测试中 Spring 的 ApplicationContext 可能是普通上下文（非 Web 环境），
    // 而 ServletWebServerApplicationContext 仅存在于 Servlet 容器启动时。
//    @Autowired
//    private ServletWebServerApplicationContext context;


    @SneakyThrows
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady(ApplicationReadyEvent event) {
        System.out.println("Spring Boot 应用已完全启动并准备好接收请求");
        ConfigurableApplicationContext context = event.getApplicationContext();
        // 获取Environment实例
        Environment env = context.getEnvironment();
        // 获取端口号和上下文路径
        {
//            Environment env = context.getBean(Environment.class);
            String ip = InetAddress.getLocalHost().getHostAddress();
            String port = env.getProperty("server.port");
            String path = env.getProperty("server.servlet.context-path");
            log.info("\n----------------------------------------------------------\n\t" +
                    "系统启动成功\n\t" +
                    "Local: \t\thttp://localhost:" + port + path + "/\n\t" +
                    "External: \thttp://" + ip + ":" + port + path + "/\n\t" +
                    "Swagger文档: \thttp://" + ip + ":" + port + path + "/doc.html\n" +
                    "----------------------------------------------------------");
        }

    }

    @SneakyThrows
    @EventListener(ServletWebServerInitializedEvent.class)
    public void onApplicationEvent(ServletWebServerInitializedEvent event) {

        ServletWebServerApplicationContext context = event.getApplicationContext();

        //查找第一个非回环的主机信息
        /*
        非回环主机信息指的是：

        排除回环地址(loopback address，如127.0.0.1)

        排除未启用的网络接口

        通常指计算机的实际网络接口(如以太网、WiFi等)的IP地址
         */
        InetUtils.HostInfo hostInfo = inetUtils.findFirstNonLoopbackHostInfo();
        String hostname = hostInfo.getHostname();
        String ip = hostInfo.getIpAddress();
        log.info("本机ip={}", ip);
        log.info("本机hostname={}", hostname);


        int port = context.getWebServer().getPort();
        System.out.println("port = " + port);
        String ip2 = InetAddress.getLocalHost().getHostAddress();
        System.out.println("ip2 = " + ip2);


        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
//            System.out.println("Interface: " + networkInterface.getName());

            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
//                System.out.println("  Address: " + address.getHostAddress());
//                System.out.println("  Address: " + address.getHostAddress());

                if (!address.isLoopbackAddress() && address instanceof Inet4Address) {
                    System.out.println("  Address: " + address.getHostAddress());
                }
            }
        }
    }
}
