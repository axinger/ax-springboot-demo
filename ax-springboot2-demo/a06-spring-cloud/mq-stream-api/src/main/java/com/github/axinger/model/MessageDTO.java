package com.github.axinger.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDTO<T> {
    private String topic;
    private String type;
    private LocalDateTime date;
    private T data;
}
