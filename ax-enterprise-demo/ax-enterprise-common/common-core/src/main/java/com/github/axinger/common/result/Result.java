package com.github.axinger.common.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一响应结果
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> implements Serializable {

    private Integer code;

    private String msg;

    private T data;

    public static <T> Result<T> ok() {
        return Result.<T>builder().code(200).msg("success").build();
    }

    public static <T> Result<T> ok(T data) {
        return Result.<T>builder().code(200).msg("success").data(data).build();
    }

    public static <T> Result<T> fail(String msg) {
        return Result.<T>builder().code(500).msg(msg).build();
    }

    public static <T> Result<T> fail(Integer code, String msg) {
        return Result.<T>builder().code(code).msg(msg).build();
    }
}
