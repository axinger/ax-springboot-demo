package com.github.axinger;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class 正则表达式Tests {

//    五、总结对比
//    特性	    String.matches()	Pattern + Matcher
//    使用难度	简单	                稍复杂
//    性能	    低（每次编译）	    高（Pattern 可复用）
//    功能	    仅判断全匹配	        支持 find、group、replace 等
//    适用场景	一次性简单校验	    复杂文本处理、频繁使用

    /// 用于判断当前字符串是否完全匹配给定的正则表达式
    @Test
    public void test1() {
        String regex = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";
        String email = "axinger@163.com";
        boolean matches = email.matches(regex);
        System.out.println("matches = " + matches);
        boolean matches1 = Pattern.matches(regex, email);
        System.out.println("matches1 = " + matches1);
    }

    @Test
    public void test2() {
        Pattern pattern = Pattern.compile("(T\\+)?(\\d+(?:\\.\\d+)?)(年|月|个月|日|天|时|小时|分|分钟|秒|Y|M|d|H|m|s)");
        List<String> list = List.of("T+1天", "T+1H", "T+1M", "T+1Y", "T+1.5天", "T+1.5H", "T+1.5M", "T+1.5Y", "2小时", "3m", "OK");
        for (String text : list) {
            Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                String index0 = matcher.group(0); // 自身字符
                String index1 = matcher.group(1); // T+, 第一个匹配的项目
                String amount = matcher.group(2);// 提取数字
                String unit = matcher.group(3); // 提取单位
                System.out.println("index1 = " + index1);
                switch (unit) {
                    case "年":
                    case "Y":
                        System.out.println("text :" + index0 + "," + "年数：" + amount);
                        break;
                    case "个月":
                    case "M":
                        System.out.println("text :" + index0 + "," + "月数：" + amount);
                        break;
                    case "天":
                        System.out.println("text :" + index0 + "," + "天数：" + amount);
                        break;
                    case "小时":
                    case "H":
                        System.out.println("text :" + index0 + "," + "小时数：" + amount);
                        break;
                    case "分钟":
                    case "m":
                        System.out.println("text :" + index0 + "," + "分钟数：" + amount);
                        break;
                    default:
                        System.out.println("无效的时间描述：" + index0);
                }
            } else {
                System.out.println("无效的时间描述：" + text);
            }
        }
    }

    @Test
    public void test3() {
        //无论单词之间有多少个空格（一个或多个），都会被当作一个分隔符处理
        // 这种用法比单纯使用空格作为分隔符更加灵活，因为它可以处理多个连续空格的情况，而不会产生空字符串元素。，
        Pattern p = Pattern.compile("\\s+");
        String[] words = p.split("hello   world  java");
        System.out.println("words = " + Arrays.toString(words));
        // 结果: ["hello", "world", "java"]
        //简写形式：
        String[] arr2 = "apple,banana,orange".split(",");

        System.out.println(Arrays.toString(arr2));

        //"a\\.b\\*c\\?"
        String quote = Pattern.quote("a.b*c?");
        System.out.println("quote = " + quote);
    }

    @Test
    public void test4() {
//        在 Pattern.compile(regex, flags) 中可加标志：
//
//        标志	作用
//        Pattern.CASE_INSENSITIVE	忽略大小写
//        Pattern.DOTALL	. 匹配包括换行符在内的所有字符
//        Pattern.MULTILINE	^ 和 $ 匹配每行开头结尾
//        Pattern.COMMENTS	支持注释和空白


        // 正则：匹配 v1, V2, v3.5 等，不区分大小写，支持多行
//        Pattern pattern = Pattern.compile(
//                "v\\d+(?:\\.\\d+)?",
//                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
//        );

        Pattern pattern = Pattern.compile(
                "v\\w*\\d+(?:\\.\\d+)?",  // ← 关键：\\w* 匹配中间的字母（如 ersion）
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
        );

        //如果你想更强大，可以支持 v-1、version: 2 等格式：
//        Pattern pattern =  Pattern.compile(
//                "v\\w*\\s*-?\\d+(?:\\.\\d+)?",
//                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
//        );

        String text =
                "这是版本v1\n" +
                        "当前是V2.5\n" +
                        "下一个版本是v3\n" +
                        "已废弃version0.9\n" +
                        "无关内容：abc123";

        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            System.out.println("找到版本: " + matcher.group());
        }
    }


    @Test
    public void testCaseInsensitive() {
        // Pattern.CASE_INSENSITIVE - 忽略大小写
        System.out.println("=== Pattern.CASE_INSENSITIVE 示例 ===");
        String text = "Hello World Java";
        Pattern pattern1 = Pattern.compile("java");
        Pattern pattern2 = Pattern.compile("java", Pattern.CASE_INSENSITIVE);

        Matcher matcher1 = pattern1.matcher(text);
        Matcher matcher2 = pattern2.matcher(text);

        System.out.println("普通匹配 'java' in 'Hello World Java': " + matcher1.find()); // false
        System.out.println("忽略大小写匹配 'java' in 'Hello World Java': " + matcher2.find()); // true
    }

    @Test
    public void testDotAll() {
        // Pattern.DOTALL - . 匹配包括换行符在内的所有字符
        System.out.println("\n=== Pattern.DOTALL 示例 ===");
        String text = "Hello\nWorld";

        Pattern pattern1 = Pattern.compile("Hello.World");
        Pattern pattern2 = Pattern.compile("Hello.World", Pattern.DOTALL);

        Matcher matcher1 = pattern1.matcher(text);
        Matcher matcher2 = pattern2.matcher(text);

        System.out.println("普通匹配 'Hello.World' in 'Hello\\nWorld': " + matcher1.matches()); // false
        System.out.println("DOTALL匹配 'Hello.World' in 'Hello\\nWorld': " + matcher2.matches()); // true
    }

    @Test
    public void testMultiline() {
        // Pattern.MULTILINE - ^ 和 $ 匹配每行开头结尾
        System.out.println("\n=== Pattern.MULTILINE 示例 ===");
        String text = "Hello\nWorld\nJava";

        Pattern pattern1 = Pattern.compile("^World$");
        Pattern pattern2 = Pattern.compile("^World$", Pattern.MULTILINE);

        Matcher matcher1 = pattern1.matcher(text);
        Matcher matcher2 = pattern2.matcher(text);

        System.out.println("普通匹配 '^World$' in 'Hello\\nWorld\\nJava': " + matcher1.find()); // false
        System.out.println("MULTILINE匹配 '^World$' in 'Hello\\nWorld\\nJava': " + matcher2.find()); // true
    }

    @Test
    public void testComments() {
        // Pattern.COMMENTS - 支持注释和空白
        System.out.println("\n=== Pattern.COMMENTS 示例 ===");
        String text = "123abc456";

        // 普通写法
        Pattern pattern1 = Pattern.compile("\\d+[a-z]+\\d+");

        // 使用 COMMENTS 标志，可以添加注释和空白提高可读性
        Pattern pattern2 = Pattern.compile(
                "\\d+     # 数字部分  \n" +
                        "[a-z]+   # 字母部分  \n" +
                        "\\d+      # 数字部分",
                Pattern.COMMENTS
        );

        Matcher matcher1 = pattern1.matcher(text);
        Matcher matcher2 = pattern2.matcher(text);

        System.out.println("普通写法匹配 '\\d+[a-z]+\\d+' in '123abc456': " + matcher1.matches()); // true
        System.out.println("COMMENTS写法匹配带注释的模式 in '123abc456': " + matcher2.matches()); // true
    }

    @Test
    public void testCombinedFlags() {
        // 组合使用多个标志
        System.out.println("\n=== 组合标志示例 ===");
        String text = "Hello\nWORLD\njava";

        // 组合使用 CASE_INSENSITIVE 和 MULTILINE 标志
        Pattern pattern = Pattern.compile("^world$", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);

        System.out.println("组合使用CASE_INSENSITIVE|MULTILINE匹配 '^world$' in 'Hello\\nWORLD\\njava': " + matcher.find()); // true
    }
}
