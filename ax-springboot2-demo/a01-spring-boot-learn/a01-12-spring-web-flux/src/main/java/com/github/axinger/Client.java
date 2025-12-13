package com.github.axinger;

import com.github.axinger.entity.User;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class Client {

    public static void main(String[] args) {
        WebClient webClient = WebClient.create("http://localhost:8082");

        Integer id = 1;
        User user = webClient.get()
                .uri("/user/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(User.class)
                .block();
        System.out.println("user = " + user);
    }
}
