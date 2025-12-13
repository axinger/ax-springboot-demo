package com.axing.common.response.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局统一返回结果类
 *
 * @author xing
 * Result 和 T用title,name不要同时使用
 * 避免bug
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "Result", description = "请求返回结果")
public class Result<T> implements Serializable {

    @Schema(title = "状态码", description = "2xx成功")
    private Integer code;

    @Schema(title = "返回数据", description = "数据")
    private T data;

    @Schema(title = "提示信息", description = "错误信息")
    private String msg = "";

    @Schema(title = "是否成功", description = "成功：true；失败：false")
    private Boolean success = false;

    @Schema(title = "拓展字段", description = "其他字段")
    private Map<String, Object> params;

    public static <T> Result<T> success(T data) {
        return Result.<T>builder()
                .code(200)
                .data(data)
                .build();
    }

    public static <T> Result<T> success() {
        return Result.<T>builder()
                .code(200)
                .build();
    }

    public static <T> Result<T> fail(Integer code, String msg) {
        return Result.<T>builder()
                .code(code)
                .msg(msg)
                .build();
    }

    public static <T> Result<T> fail() {
        return Result.fail(400, "操作失败");
    }

    public static <T> Result<T> fail(String msg) {
        return Result.fail(400, msg);
    }

    // 动态属性,不解析, 方法重载,和params的set方法名一样
    @com.fasterxml.jackson.annotation.JsonAnySetter
    public Result<T> setParams(String key, Object value) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
        return this;
    }

    @com.fasterxml.jackson.annotation.JsonAnyGetter
    public Map<String, Object> getParams() {
        return params;
    }

    public Boolean getSuccess() {
        return (code != null && code >= 200 && code < 300);
    }
}
