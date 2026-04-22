package com.axing.common.redis.util;

import cn.hutool.core.util.StrUtil;

public class RedisKeyUtil {

    /**
     * 使用: 分隔,组成key
     *
     * @param kes
     * @return
     */
    public static String getKey(Object... kes) {
        return StrUtil.join(":", kes);
    }
}
