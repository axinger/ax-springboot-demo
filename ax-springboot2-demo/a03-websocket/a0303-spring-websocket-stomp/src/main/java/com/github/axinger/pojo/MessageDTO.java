package com.github.axinger.pojo;

import lombok.Data;

/**
 * @Author JCccc
 * @Description
 * @Date 2021/8/20 9:26
 */
@Data
public class MessageDTO {

    /**
     * 消息编码
     */
    private String code;

    /**
     * 来自（保证唯一）
     */
    private String form;

    /**
     * 去自（保证唯一）
     */
    private String to;

    /**
     * 内容
     */
    private String content;

}
