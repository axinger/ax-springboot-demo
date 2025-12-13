package com.axing.common.response.exception;

import com.axing.common.response.dto.ResultCodeEnum;
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
    private Integer code;

    /**
     * 通过状态码和错误消息创建异常对象
     */

    public ServiceException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(ResultCodeEnum codeEnum) {
        super(codeEnum.getMessage());
        this.code = codeEnum.getCode();
    }

    public static ServiceException message(String message) {
        return new ServiceException(201, message);
    }

}
