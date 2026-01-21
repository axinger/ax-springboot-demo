package com.github.axinger;

import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.github.f4b6a3.uuid.UuidCreator;
import com.github.f4b6a3.uuid.util.UuidTime;
import com.github.f4b6a3.uuid.util.UuidUtil;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

public class UUIDTests {

    @Test
    public void test() {
        UUID timeOrderedEpochFast = UuidCreator.getTimeOrderedEpochFast();
        System.out.println(timeOrderedEpochFast);
    }

    @Test
    public void test02() {
        ///  // 方案一：指定具体的时间点（例如：指定为昨天的时间）
        Instant specificTime = Instant.parse("2025-10-17T12:00:00Z");
//        Instant specificTime = LocalDateTimeUtil.parse("2025-10-17 12:00:00", "yyyy-MM-dd HH:mm:ss").toInstant(ZoneOffset.UTC);
        UUID specificId = UuidCreator.getTimeOrderedEpoch(specificTime);
        System.out.println("specificId = " + specificId);
        // 0199f20a-8600-7000-957f-ec09ecda748b
        // 0199f20a-8600-7000-abc0-d7f9def90155
    }

    @Test
    public void test03() {
        // 方案二：指定比当前时间更早（例如：当前时间减去 1小时）
        Instant pastTime = Instant.now().minusSeconds(3600);
        UUID pastId = UuidCreator.getTimeOrderedEpoch(pastTime);
        System.out.println("pastId = " + pastId);
    }

    @Test
    public void test04() {
        ///  // 方案一：指定具体的时间点（例如：指定为昨天的时间）
        Instant specificTime = Instant.parse("2025-10-17T12:00:00Z");
        UUID specificId1 = UuidCreator.getTimeOrderedEpoch(specificTime);
        System.out.println("specificId1 = " + specificId1);


        Instant specificTime2 = Instant.parse("2025-10-17T13:00:00Z");
        UUID specificId2 = UuidCreator.getTimeOrderedEpoch(specificTime2);
        System.out.println("specificId2 = " + specificId2);


//        long timestampMillis = UuidTime.getEpochMilli(specificId2); // 关键方法！
        long timestamp = UuidUtil.getTimestamp(specificId2);

        LocalDateTime localDateTime = LocalDateTimeUtil.of(timestamp);
        System.out.println("localDateTime = " + localDateTime);

        Instant instant3 = UuidUtil.getInstant(specificId2);
        System.out.println("instant3 = " + instant3);

        Instant earlierTime = instant3.plusMillis(-1);
        UUID specificId3 = UuidCreator.getTimeOrderedEpoch(earlierTime);

        System.out.println("specificId3 = " + specificId3.compareTo(specificId2));

        int compare = CompareUtil.compare(specificId3, specificId2);
        System.out.println("compare = " + compare);

    }
}
