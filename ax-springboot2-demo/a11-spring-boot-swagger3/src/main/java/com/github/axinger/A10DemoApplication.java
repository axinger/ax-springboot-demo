package com.github.axinger;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.*;
import java.net.URI;

@SpringBootApplication
public class A10DemoApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(A10DemoApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String url = "http://localhost:11010";
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down...");
        }));

        // 打开系统浏览器
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(url));
        } else {
            Runtime.getRuntime().exec("cmd /c start " + url);
        }
    }
}
