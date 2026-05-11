package com.axinger.shared;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

/**
 * 客户ID值对象
 */
@Embeddable
public record CustomerId(UUID value) implements Serializable {

    public CustomerId {
        if (value == null) {
            throw new IllegalArgumentException("客户ID不能为空");
        }
    }

    public static CustomerId create() {
        return new CustomerId(UUID.randomUUID());
    }

    public static CustomerId fromString(String id) {
        return new CustomerId(UUID.fromString(id));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}