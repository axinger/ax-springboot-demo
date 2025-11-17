package com.github.axinger.config;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.github.f4b6a3.uuid.UuidCreator;
import org.springframework.stereotype.Component;

@Component
public class MyUUIDGenerator extends DefaultIdentifierGenerator {

    // @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @Override
    public String nextUUID(Object entity) {
        // 返回带横线的标准 UUID
//        return UUID.randomUUID().toString();
        return UuidCreator.getTimeOrderedEpochFast().toString().replace("-", "");
//        return UuidCreator.getTimeOrderedEpochFast().toString();
        // 或者返回无横线：return UUID.randomUUID().toString().replace("-", "");
    }
}
