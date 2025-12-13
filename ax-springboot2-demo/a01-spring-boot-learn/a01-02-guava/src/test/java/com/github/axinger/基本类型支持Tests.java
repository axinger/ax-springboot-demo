package com.github.axinger;

import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;
import com.google.common.primitives.UnsignedInts;
import org.junit.jupiter.api.Test;

public class 基本类型支持Tests {

    @Test
    public void test1() {

        byte[] byteArray = {1, 2, 3};
        // 使用 Bytes.asList 方法将 byte 数组转换为 List<Byte>
        java.util.List<Byte> byteList = Bytes.asList(byteArray);
        System.out.println("Byte list: " + byteList);


        // 转换 int 数组到 byte 数组
        int[] intArray = {1, 2, 3};

        // 查找 int 数组中的最大值
        int maxInt = Ints.max(intArray);

        System.out.println("Max int: " + maxInt);
    }
    //将有符号整数作为无符号整数进行处理。

    @Test
    public void test2() {
        String unsignedIntString = "4294967295"; // 最大的无符号 int
        long value = UnsignedInts.parseUnsignedInt(unsignedIntString);
        System.out.println("Parsed unsigned int: " + value);

        // 反向操作：将无符号整数转换回字符串表示
        String backToString = UnsignedInts.toString((int) value);
        System.out.println("Unsigned int to string: " + backToString);

    }
}
