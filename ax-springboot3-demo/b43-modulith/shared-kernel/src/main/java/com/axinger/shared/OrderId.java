package com.axinger.shared;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

/**
 * 订单ID值对象
 */
@Embeddable
public record OrderId(UUID value) implements Serializable {

    public OrderId {
        if (value == null) {
            throw new IllegalArgumentException("订单ID不能为空");
        }
    }

    public static OrderId create() {
        return new OrderId(UUID.randomUUID());
    }

    public static OrderId fromString(String id) {
        return new OrderId(UUID.fromString(id));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}