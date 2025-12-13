package com.axing.common.response.dto;

import lombok.Getter;

import java.io.Serializable;

/**
 * 统一返回结果状态信息类
 *
 * @author xing
 */
@Getter
public enum ResultCodeEnum implements Serializable {

    SUCCESS(200, "成功"),

    FAIL(201, "失败"),

    PARAM_ERROR(202, "参数不正确"),

    SERVICE_ERROR(203, "服务异常"),

    DATA_ERROR(204, "数据异常"),

    ATA_UPDATE_ERROR(205, "数据版本异常"),

    LOGIN_AUTH(208, "未登陆"),

    PERMISSION(209, "没有权限"),

    CODE_ERROR(210, "验证码错误"),

    LOGIN_DISABLED_ERROR(212, "改用户已被禁用"),

    REGISTER_MOBLE_ERROR(213, "手机号已被使用"),

    LOGIN_AURH(214, "需要登录"),

    LOGIN_ACL(215, "没有权限"),

    URL_ENCODE_ERROR(216, "URL编码失败"),

    ILLEGAL_CALLBACK_REQUEST_ERROR(217, "非法回调请求"),

    FETCH_ACCESSTOKEN_FAILD(218, "获取accessToken失败"),

    FETCH_USERINFO_ERROR(219, "获取用户信息失败"),

    FAILED(500, "操作失败"),

    VALIDATE_FAILED(404, "参数检验失败"),

    UNAUTHORIZED(401, "暂未登录或token已经过期"),

    FORBIDDEN(403, "没有相关权限");

    private final Integer code;
    private final String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String toString() {
        return "{" + "code=" + code + ", message='" + message + '\'' + '}';
    }
}
