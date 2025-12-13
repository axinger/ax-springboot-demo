package com.github.axinger.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Order {
    private String id;
    private String customerId;
    private double amount;
    private String status;
}
