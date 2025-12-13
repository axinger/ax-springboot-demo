package com.github.axinger;

import lombok.Data;

@Data
public class OrderEvent {
    private String orderId;
    private String userId;
    private double price;
    private String status;
}
