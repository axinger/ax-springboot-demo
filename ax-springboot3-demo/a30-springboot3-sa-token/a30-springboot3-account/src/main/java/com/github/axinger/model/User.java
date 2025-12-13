package com.github.axinger.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    /**
     * id
     */
    private String id;

    /**
     * 账号
     */
    private String userName;

    /**
     * 密码
     */
    private String password;
}
