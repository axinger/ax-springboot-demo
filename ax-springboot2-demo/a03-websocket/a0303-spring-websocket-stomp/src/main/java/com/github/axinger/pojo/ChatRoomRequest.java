package com.github.axinger.pojo;

import lombok.Data;

@Data
public class ChatRoomRequest {
    private String userId;
    private String name;
    private String chatValue;
}
