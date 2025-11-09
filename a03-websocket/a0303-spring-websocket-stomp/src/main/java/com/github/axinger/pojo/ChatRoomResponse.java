package com.github.axinger.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChatRoomResponse {
    private String userId;
    private String name;
    private String chatValue;
    private LocalDateTime date;
}
