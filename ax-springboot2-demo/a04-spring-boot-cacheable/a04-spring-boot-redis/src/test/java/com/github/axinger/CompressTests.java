package com.github.axinger;

import cn.hutool.core.codec.Base64;
import com.github.axinger.util.CompressUtil;
import org.junit.jupiter.api.Test;

public class CompressTests {

    @Test
    public void testCompress() {
        byte[] bytes = CompressUtil.serialize("abcd");

        System.out.println("压缩数据" + new String(bytes));
        boolean base64 = Base64.isBase64(bytes);
        System.out.println("是否是base64编码" + base64);

        String deserialize = CompressUtil.deserialize(bytes);
        System.out.println("deserialize = " + deserialize);
    }
}
