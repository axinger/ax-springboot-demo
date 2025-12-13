package com.github.axinger.desensitization.dto;

import com.github.axinger.desensitization.annotation.UnSensitive;
import com.github.axinger.desensitization.model.SensitiveStrategy;
import lombok.Data;

@Data
public class User {
    @UnSensitive(strategy = SensitiveStrategy.USERNAME)
    private String username;
    @UnSensitive(strategy = SensitiveStrategy.ID_CARD)
    private String idcard;
    @UnSensitive(strategy = SensitiveStrategy.PHONE)
    private String phone;
    @UnSensitive(strategy = SensitiveStrategy.ADDRESS)
    private String address;

    // setter getter代码被删除
}
