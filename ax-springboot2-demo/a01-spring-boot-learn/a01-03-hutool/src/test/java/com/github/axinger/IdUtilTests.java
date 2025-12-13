package com.github.axinger;

import cn.hutool.core.util.IdUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName UuidTests.java
 * @description TODO
 * @createTime 2022年07月28日 14:00:00
 */

public class IdUtilTests {


    @Test
    void test_uuid() {

        System.out.println("IdUtil.simpleUUID() = " + IdUtil.simpleUUID());

        System.out.println("UUID.fastUUID() = " + UUID.randomUUID());

        System.out.println("IdUtil.fastUUID() = " + IdUtil.fastUUID());

        // getSnowflake 单利
        // return Singleton.get(Snowflake.class);
        System.out.println("IdUtil.getSnowflakeNextId() = " + IdUtil.getSnowflakeNextId());

        System.out.println("IdUtil.objectId() = " + IdUtil.objectId());


        System.out.println("IdUtil.getDataCenterId(1) = " + IdUtil.nanoId());

        System.out.println("IdUtil.getSnowflake().nextIdStr() = " + IdUtil.getSnowflake().nextIdStr());
        System.out.println("IdUtil.getSnowflake().nextId() = " + IdUtil.getSnowflake().nextId());
        System.out.println("IdUtil.getSnowflake().getGenerateDateTime(1) = " + IdUtil.getSnowflake().getGenerateDateTime(1));

        System.out.println("IdUtil.getSnowflakeNextIdStr() = " + IdUtil.getSnowflakeNextIdStr());
    }


}
