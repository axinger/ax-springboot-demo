package com.github.axinger.service2.impl;

import cn.hutool.core.util.StrUtil;
import com.github.axinger.annotation.ApiVersion;
import org.springframework.stereotype.Service;

@Service
@ApiVersion
public class AddService {
    public String add3(String a, String b) {
        return StrUtil.format("add3:{}_{}", a, b);
    }
}
