package com.github.axinger;

import cn.hutool.core.util.PageUtil;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class PageUtilTest {

    @Test
    public void test() {


        int[] rainbow = PageUtil.rainbow(3, 100, 10);
        System.out.println("rainbow = " + Arrays.toString(rainbow));
    }

    @Test
    public void test2() {
        int total = 10; // 总数据量
        int pageSize = 3; // 每页显示的数据量

        // 计算总页数
        int totalPages = PageUtil.totalPage(total, pageSize);
        System.out.println("totalPages = " + totalPages);

        // 输出每页的起始和结束索引
        for (int i = 0; i < totalPages; i++) {
            System.out.println("limit " + i + "," + pageSize);

            System.out.println("getStart = " + PageUtil.getStart(i, pageSize));
            System.out.println("getEnd = " + PageUtil.getEnd(i, pageSize));
        }


    }

    @Test
    public void test3() {
        int total = 10; // 总数据量
        int pageSize = 3; // 每页显示的数据量

        // 计算总页数
        int totalPages = PageUtil.totalPage(total, pageSize);

        // 输出每页的起始和结束索引
        for (int i = 0; i < totalPages; i++) {

            int[] startEnd = PageUtil.transToStartEnd(i, pageSize);
            System.out.println("Page " + i + ": [" + startEnd[0] + ", " + startEnd[1] + "]");
        }


    }
}
