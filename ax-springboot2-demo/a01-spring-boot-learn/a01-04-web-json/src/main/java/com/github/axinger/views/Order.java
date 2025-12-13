package com.github.axinger.views;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.axinger.model.Views;

import java.math.BigDecimal;

public class Order {
    @JsonView(Views.Public.class)
    private String orderId;

    @JsonView(Views.Internal.class)
    private User customer;  // 会使用User类上的视图配置

    @JsonView(Views.Admin.class)
    private BigDecimal discount;
}
