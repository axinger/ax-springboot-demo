package com.github.axinger.model;

import lombok.Data;

@Data
public class MessageDTO {
    private String fromUserId;
    private String toUserId;
    private String data;
}
