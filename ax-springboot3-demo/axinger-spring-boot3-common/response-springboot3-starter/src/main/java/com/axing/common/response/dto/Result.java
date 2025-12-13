package com.axing.common.response.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 全局统一返回结果类
 *
 * @author xing
 * Result 和 T用title,name不要同时使用
 * 避免bug
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "Result", description = "请求返回结果")
public class Result<T> implements Serializable {

    @Schema(title = "状态码", description = "成功：200；失败：201")
    private Integer code = 200;

    @Schema(title = "是否成功", description = "成功：true；失败：false")
    private Boolean success = false;

    @Schema(title = "提示信息", description = "错误信息")
    private String msg = "";

    @Schema(title = "返回数据", description = "数据")
    private T data;


    public static <T> Result<T> ok(T data) {
        Result<T> result = new Result<>();
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setData(data);
        return result;
    }

    public static <T> Result<T> ok() {
        return Result.ok(null);
    }

    public static <T> Result<T> ok(String msg) {
        Result<T> result = new Result<>();
        result.setCode(ResultCodeEnum.SUCCESS.getCode());
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> fail(String msg) {
        Result<T> result = new Result<>();
        result.setCode(ResultCodeEnum.FAIL.getCode());
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> fail() {
        return Result.fail(null);
    }

}
