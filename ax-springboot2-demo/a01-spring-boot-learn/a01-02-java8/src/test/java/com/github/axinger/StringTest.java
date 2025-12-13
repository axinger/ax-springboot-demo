package com.github.axinger;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.TreeSet;

public class StringTest {


    String string = "good";
    char[] chars = {'t', 'e', 's', 't'};
    StringBuffer string2 = new StringBuffer("good");
    private Long totalSpent;

    public static void main(String[] args) {

        TreeSet<String> set = new TreeSet<>();

        set.add("3");
        set.add("1");
        set.add("10");
        System.out.println("set = " + set);

        StringTest test = new StringTest();
        test.change(test.string, test.chars);
        System.out.println("test.string = " + test.string);
        System.out.println(test.chars);


        test.change2(test.string2);
        System.out.println("test.string2 = " + test.string2);

        System.out.println(1 << 1);
        System.out.println(1 << 2);
        System.out.println(1 << 3);

        System.out.println(2 << 1);
        System.out.println(2 << 2);


        StringBuffer string2 = new StringBuffer("good");
        System.out.println("delete = " + string2.delete(2, 6));
    }

    public void antMatchers(String... antPatterns) {
        System.out.println("antPatterns = " + antPatterns);
    }

    @Test
    void test_antMatchers() {
        List<String> list = ListUtil.of("1", "2");

        String[] objects = list.toArray(new String[list.size()]);
//        antMatchers(objects);
        antMatchers("1", "2");
    }

    @Test
    void test_str() {
//        String s = "1/2-103";
//        System.out.println("s = " + s);


        BigDecimal run = new BigDecimal(String.valueOf(21 * 60 * 60)).divide(new BigDecimal("3600000"), 2, RoundingMode.HALF_UP);

        System.out.println("run = " + run);
    }

    @Test
    void test_Boolean() {

        System.out.println("Boolean.valueOf = " + Boolean.valueOf("d"));
        System.out.println("Boolean.valueOf = " + Boolean.valueOf("true"));
        System.out.println("Boolean.valueOf = " + Boolean.valueOf("1"));
        System.out.println("Boolean.valueOf = " + Boolean.valueOf("1.0"));

        Object obj;
        obj = "false";
        System.out.println("Boolean.valueOf(obj.toString()) = " + Boolean.valueOf(obj.toString()));

    }

    @Test
    void test_BigDecimal() {
        BigDecimal stopTime = BigDecimal.ZERO;
//         long totalSpent;
        stopTime = stopTime.add(new BigDecimal(totalSpent));

        System.out.println("stopTime = " + stopTime);
    }

    @Test
    void test_format() {

//       String url = String.format("%s/%s", "aaaa", "b");
//        System.out.println("url = " + url);
        String val = "1";
        System.out.println("Boolean.parseBoolean(val) = " + Boolean.parseBoolean(val));
//
        String val2 = "1";
        if (StrUtil.isNumeric(val2)) {
            System.out.println("Integer.parseInt(val) = " + Integer.parseInt(val2));
        }

    }

    @Test
    void test2() {
        String str = "http://localhost";


//        //判断最后一个字符是否为“段”
//        if (str.endsWith("/")) {
//            str = str.substring(0,str.length() - 1);
//        }


        System.out.println("str = " + str);
        final String s = StringUtils.trimTrailingCharacter(str, '/');
        System.out.println("s = " + s);


        final String th = StringUtils.replace(str, "https", "tcp");
        System.out.println("th = " + th);
        System.out.println("str = " + str);

    }

    public void change(String string, char[] ch) {
        string = "test ok";
        ch[0] = 'b';
    }

    public void change2(StringBuffer string2) {
        string2 = new StringBuffer("test ok");

    }


}
