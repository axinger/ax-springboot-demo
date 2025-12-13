package com.github.axinger;

import lombok.Data;

@Data
public class OrderRequest {
    private String userId;
    private double price;
}
