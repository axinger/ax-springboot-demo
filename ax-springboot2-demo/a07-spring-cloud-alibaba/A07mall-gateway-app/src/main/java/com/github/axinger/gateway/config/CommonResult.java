package com.github.axinger.gateway.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult<T> {
    private int code;
    private String message;
    private T data;
    private long timestamp = System.currentTimeMillis();

    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<>(200, "成功", data, System.currentTimeMillis());
    }

    public static CommonResult<?> error(int code, String message) {
        return new CommonResult<>(code, message, null, System.currentTimeMillis());
    }
}
