import com.sun.net.httpserver.HttpServer;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.concurrent.Executors;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/*
⚠️ 注意：不要在线程池中复用虚拟线程（如 ThreadPoolExecutor），因为它们本就是“按需创建、用完即弃”的。
 */
public class 虚拟线程Tests {

    @Test
    public void test01() {
        Thread.ofVirtual()
                .name("MyTask-" + "AAA")  // ← 自定义名称
                .start(() -> {
            System.out.println("i = " + Thread.currentThread().getName()+",isVirtual="+ Thread.currentThread().isVirtual());
            System.out.println("hello world");
        });
    }

    @SneakyThrows
    public static String doSomething(int i) {
//        System.out.println("i = " + Thread.currentThread().getName());
        TimeUnit.SECONDS.sleep(1);
        return "result " + i;
    }
    @Test
    public void test02() {
        // 方式2：使用 Executors.newVirtualThreadPerTaskExecutor()
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 10_000).forEach(i -> {
                executor.submit(() -> {
                    System.out.println("i = " + Thread.currentThread().getName()+"isVirtual="+ Thread.currentThread().isVirtual());

                    // 模拟 I/O 或计算任务
                    return doSomething(i);
                });
            });
        } // 自动 join 所有虚拟线程
    }

    @Test
    public void test03() {

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            server.createContext("/hello", exchange -> {
                // 每个请求在一个虚拟线程中处理
                String response = "Hello at " + Instant.now();
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
                exchange.close();
            });

            server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
            server.start();
            System.out.println("Server started on http://localhost:8080");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}
