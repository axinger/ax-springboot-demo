package com.github.axinger;


import org.apache.commons.text.RandomStringGenerator;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.commons.text.StringSubstitutor;
import org.apache.commons.text.WordUtils;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

class CommonsTextTest {

    @Test
    void test1() {

        // 1. 准备变量映射
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("name", "Alice");
        valuesMap.put("os", "Linux");

        // 2. 包含占位符的模板字符串
        String templateString = "Welcome, ${name}! Your system is ${os}.";

        // 3. 创建 StringSubstitutor 并执行替换
        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        String resolvedString = sub.replace(templateString);

        System.out.println(resolvedString);
        // 输出： Welcome, Alice! Your system is Linux.
    }

    @Test
    void test2() {
        //示例 2：自定义前缀和后缀（例如使用 #{variable}）
        String templateString = "Hello, #{user}!";
        Map<String, String> valuesMap = Map.of("user", "Bob"); // Java 9+

        StringSubstitutor sub = new StringSubstitutor(valuesMap, "#{", "}");
        String result = sub.replace(templateString);
        System.out.println(result); // 输出： Hello, Bob!
    }

    @Test
    void test3() {
//        b. 计算字符串相似度
//        FuzzyScore 和 LevenshteinDistance 是常用的算法。
//
//        示例：Levenshtein 距离（编辑距离）
//        编辑距离是指将一个字符串转换为另一个字符串所需的最少单字符编辑（插入、删除、替换）次数。
        LevenshteinDistance lev = new LevenshteinDistance();

        int distance1 = lev.apply("kitten", "sitting");
        System.out.println(distance1); // 输出： 3 (k->s, e->i, +g)

        int distance2 = lev.apply("apple", "apple");
        System.out.println(distance2); // 输出： 0

        String s1 = "hello world";
        String s2 = "hallo wor1d";
        int distance3 = lev.apply(s1, s2);
        System.out.println(distance3); // 输出： 2 (e->a, l->1)
    }

    @Test
    void test4() {
//        c. 转义与反转义
//        提供用于不同上下文（如HTML, XML, CSV, Java属性文件）的转义工具。
        // HTML 转义
        String originalHtml = "\"Hello & World <br>\"";
        String escapedHtml = StringEscapeUtils.escapeHtml4(originalHtml);
        System.out.println(escapedHtml);
        // 输出： &quot;Hello &amp; World &lt;br&gt;&quot;

        // HTML 反转义
        String unescapedHtml = StringEscapeUtils.unescapeHtml4(escapedHtml);
        System.out.println(unescapedHtml);
        // 输出： "Hello & World <br>"

        // CSV 转义（处理逗号和引号）
        String csvCell = "Value, with comma and \"quote\"";
        String escapedCsv = StringEscapeUtils.escapeCsv(csvCell);
        System.out.println(escapedCsv);
        // 输出： "Value, with comma and ""quote"""

        String unescapedCsv = StringEscapeUtils.unescapeCsv(escapedCsv);
        System.out.println(unescapedCsv);
        // 输出： Value, with comma and "quote"
    }

    @Test
    void test5() {
//        d. 字符串随机生成
//        RandomStringGenerator 可以方便地生成符合特定规则的随机字符串。
//
//        示例：生成随机字母数字字符串

        // 使用构建器模式创建生成器
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('0', 'z') // 字符范围
                .filteredBy(Character::isLetterOrDigit) // 过滤器：只保留字母和数字
                .build();

        String randomString = generator.generate(12); // 生成长度为12的字符串
        System.out.println("Random String: " + randomString);
        // 输出类似： Random String: aX3fG7hK92Lm
    }

    @Test
    void test6() {
//        e. 单词换行
//        WordUtils 提供了一些简单的单词级操作（注意：它功能较少，复杂排版请用其他库）。
//
//        示例：单词换行和首字母大写
        String longText = "The quick brown fox jumps over the lazy dog";
//        String longText = " 一二三四 五六 七八九 十"; // 中文效果不行

        // 将长文本在指定宽度处换行
        //  的默认行为是在空格处进行断行，它会尽量保证一个完整的单词（在英文中由空格分隔）不被拆开到两行
        String wrappedText = WordUtils.wrap(longText, 8);
        System.out.println(wrappedText);
//        The quick brown
//        fox jumps over
//        the lazy dog
        // 将每个单词的首字母大写
        String capitalized = WordUtils.capitalizeFully("hello world from commons-text");
        System.out.println(capitalized);
        // 输出： Hello World From Commons Text

        // 获取每个单词的首字母缩写
        String initials = WordUtils.initials("Apache Commons Text");
        System.out.println(initials);
        // 输出： ACT
    }
}
