package com.axing.common.response.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 自定义全局异常类
 *
 * @author xing
 * @date 2022/3/20 01:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
//@Schema(description = "自定义全局异常类")
@ToString
public class ServiceException extends RuntimeException {

    //    @Schema(title = "异常状态码", description = "200 正常,不会显示这里")
    private Boolean success;

    /**
     * 通过状态码和错误消息创建异常对象
     */

    public ServiceException(Boolean success, String message) {
        super(message);
        this.success = success;
    }


    public static ServiceException message(String message) {
        return new ServiceException(false, message);
    }

}
