package com.github.axinger;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class CheckTests {

    @Test
    void test() {


        // Preconditions.checkNotNull(null,"不能为空");
        Preconditions.checkNotNull(null, "不能为空 = ", "1");
        // List list = new ArrayList();
        // list.add("a");
        // System.out.println("checkElementIndex = " + Preconditions.checkElementIndex(1, list.size(), "越界"));
    }


    @Test
    void test1() {

        List list = new ArrayList();
        list.add("a");

        Integer firstNonNull = MoreObjects.firstNonNull(null, 1);
        System.out.println("返回第一个不为空的 = " + firstNonNull);


        // toStringHelper 返回类名
        System.out.println("MoreObjects.toStringHelper(UserInfo.class) = " + MoreObjects.toStringHelper(UserInfo.class));


        MoreObjects.ToStringHelper toStringHelper = MoreObjects.toStringHelper(list);
        System.out.println("toStringHelper = " + toStringHelper);


//        List<String> list = Arrays.asList(new String[10]);
//        list.set(1,"aa");
//        list.add("d");
//        System.out.println(list);

    }
}
