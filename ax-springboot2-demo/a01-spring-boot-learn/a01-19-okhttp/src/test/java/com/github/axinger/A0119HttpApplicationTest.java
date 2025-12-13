package com.github.axinger;

import com.github.axinger.api.ApiService;
import com.github.axinger.api.User;
import lombok.SneakyThrows;
import lombok.val;
import okhttp3.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class A0119HttpApplicationTest {


    @Autowired
    ApiService apiService;

    @Test
    public void test() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.example.com")
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            response.body().string();
        } catch (IOException e) {

        }


        Request request2 = new Request.Builder()
                .url("https://api.example.com")
                .header("Authorization", "Bearer token123")
                .post(RequestBody.create("Hello", MediaType.get("text/plain")))
                .build();
    }

    @Test
    public void tes2() {

        OkHttpClient client = new OkHttpClient();

        String json = "{\"name\":\"John Doe\",\"age\":30}";
        Map<String, Object> map = Map.of("name", "John Doe", "age", 30);

        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        RequestBody requestBody = RequestBody.create(map.toString(), JSON);


        Request request = new Request.Builder()
                .url("https://api.example.com")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            response.body().string();
        } catch (IOException e) {

        }
    }

    @SneakyThrows
    @Test
    public void tes3() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.example.com")
                .build();

        // 同步请求（阻塞）
        Response response = client.newCall(request).execute();

        // 异步请求（非阻塞）
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void test4() {

        Request request = new Request.Builder()
                .url("https://api.example.com")
                .build();


        val client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {

                    return chain.proceed(request);
                })

                .build();


    }

    @Test
    public void test5() {
        Request request = new Request.Builder()
                .url("https://api.example.com")
                .build();
        OkHttpClient client = new OkHttpClient();
        WebSocket webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onMessage(WebSocket webSocket, String text) {
                System.out.println("Received: " + text);
            }
        });
    }

    @SneakyThrows
    @Test
    public void test6() {
        retrofit2.Call<User> call = apiService.getUser(1);
        retrofit2.Response<User> response = call.execute();// 同步执行
        User user = response.body();
    }

    //异步请求
    @Test
    public void test7() {
//
//        retrofit2.Call<User> call = apiService.getUser(1);
//
//
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//
//            }
//
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//
//            }
//
//        });
    }
}
