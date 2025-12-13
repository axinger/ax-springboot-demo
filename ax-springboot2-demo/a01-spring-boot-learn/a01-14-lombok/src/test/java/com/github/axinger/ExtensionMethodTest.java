package com.github.axinger;

import com.github.axinger.extensions.Extensions;
import com.github.axinger.extensions.NullSafeExtensions;
import com.github.axinger.extensions.StringExtensions;
import com.github.axinger.model.User;
import lombok.experimental.ExtensionMethod;
import org.junit.Test;

import java.util.List;

@ExtensionMethod({Extensions.class, StringExtensions.class, NullSafeExtensions.class})
public class ExtensionMethodTest {


    @Test
    public void test() {
        String str = "hello";
        // 调用扩展方法
        System.out.println(str.toTitleCase());

        Integer number = 5;
        System.out.println(number.square());


        // 示例 1: 处理可能为 null 的字符串
        String nullableString = null;
        System.out.println(nullableString.orEmpty()); // 输出空字符串 ""

        // 示例 2: 处理可能为 null 的集合
        List<String> nullableList = null;
        nullableList.orEmpty().forEach(System.out::println); // 无 NPE

        // 示例 3: 安全链式调用
        User user = null;
        String city = user.safeCall(u -> u.getAddress().getCity(), null);
        System.out.println("city = " + city); // 输出 "Unknown"（无 NPE）


        User user2 = new User();
        String areaName = user2.safeCall(u -> u.getAddress().getArea().getName(), "123");
        System.out.println("areaName = " + areaName);

    }
}
