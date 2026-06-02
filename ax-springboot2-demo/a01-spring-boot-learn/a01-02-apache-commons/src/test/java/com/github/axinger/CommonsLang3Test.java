package com.github.axinger;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Date;

class CommonsLang3Test {

    // ==================== StringUtils ====================

    @Test
    void testStringUtils_isBlank() {
        // 判断字符串是否为空、null 或仅包含空白字符
        System.out.println(StringUtils.isBlank(null));      // true
        System.out.println(StringUtils.isBlank(""));        // true
        System.out.println(StringUtils.isBlank("   "));     // true
        System.out.println(StringUtils.isBlank("abc"));     // false
    }

    @Test
    void testStringUtils_trim() {
        // 去空处理，null 安全
        System.out.println(StringUtils.trim(null));         // null
        System.out.println(StringUtils.trim("  abc  "));    // abc
        System.out.println(StringUtils.trimToEmpty(null));  // ""
    }

    @Test
    void testStringUtils_defaultString() {
        // 默认值处理
        System.out.println(StringUtils.defaultString(null, "default"));     // default
        System.out.println(StringUtils.defaultString("hello", "default"));  // hello
        System.out.println(StringUtils.defaultIfBlank("  ", "fallback"));   // fallback
    }

    @Test
    void testStringUtils_substring() {
        // 安全截取，不会抛 IndexOutOfBoundsException
        String str = "Hello World";
        System.out.println(StringUtils.substring(str, 0, 5));   // Hello
        System.out.println(StringUtils.substring(str, 6));      // World
        System.out.println(StringUtils.substring(str, 0, 100)); // Hello World (不会越界)
        System.out.println(StringUtils.left(str, 4));           // Hell
        System.out.println(StringUtils.right(str, 5));          // World
    }

    @Test
    void testStringUtils_contains() {
        // 包含判断，null 安全
        System.out.println(StringUtils.contains("Hello", "ell"));       // true
        System.out.println(StringUtils.containsIgnoreCase("Hello", "ELL")); // true
        System.out.println(StringUtils.startsWith("Hello", "He"));      // true
        System.out.println(StringUtils.endsWith("Hello", "lo"));        // true
    }

    @Test
    void testStringUtils_join() {
        // 数组/集合拼接为字符串
        String[] arr = {"a", "b", "c"};
        System.out.println(StringUtils.join(arr, "-"));         // a-b-c
        System.out.println(StringUtils.join(arr, ", "));        // a, b, c
    }

    @Test
    void testStringUtils_replace() {
        // 替换操作
        System.out.println(StringUtils.replace("abcabc", "a", "X"));      // XbcXbc
        System.out.println(StringUtils.replaceOnce("abcabc", "a", "X"));  // Xbcabc
        System.out.println(StringUtils.deleteWhitespace("a b  c"));       // abc
    }

    @Test
    void testStringUtils_abbreviate() {
        // 缩略字符串
        String longStr = "This is a very long string";
        System.out.println(StringUtils.abbreviate(longStr, 10)); // This is...
    }

    @Test
    void testStringUtils_reverse() {
        // 反转字符串
        System.out.println(StringUtils.reverse("abcd")); // dcba
    }

    @Test
    void testStringUtils_difference() {
        // 比较差异
        System.out.println(StringUtils.difference("abc", "abd")); // d
        System.out.println(StringUtils.getCommonPrefix("abc", "abd")); // ab
    }

    // ==================== ArrayUtils ====================

    @Test
    void testArrayUtils() {
        int[] arr = {1, 2, 3};

        // 判断是否为空
        System.out.println(ArrayUtils.isEmpty(arr));        // false
        System.out.println(ArrayUtils.isEmpty(new int[0])); // true

        // 添加元素
        int[] newArr = ArrayUtils.add(arr, 4);
        System.out.println(ArrayUtils.toString(newArr));    // {1,2,3,4}

        // 删除元素
        int[] removed = ArrayUtils.removeElement(arr, 2);
        System.out.println(ArrayUtils.toString(removed));   // {1,3}

        // 合并数组
        int[] merged = ArrayUtils.addAll(new int[]{1, 2}, new int[]{3, 4});
        System.out.println(ArrayUtils.toString(merged));    // {1,2,3,4}

        // 包含判断
        System.out.println(ArrayUtils.contains(arr, 2));    // true

        // 获取长度（null 安全）
        System.out.println(ArrayUtils.getLength(null));     // 0
        System.out.println(ArrayUtils.getLength(arr));      // 3
    }

    // ==================== RandomStringUtils ====================

    @Test
    void testRandomStringUtils() {
        // 生成指定长度的随机字符串
        System.out.println(RandomStringUtils.random(10));                    // 随机10个字符
        System.out.println(RandomStringUtils.randomAlphabetic(10));          // 纯字母
        System.out.println(RandomStringUtils.randomAlphanumeric(10));        // 字母+数字
        System.out.println(RandomStringUtils.randomNumeric(6));              // 纯数字
        System.out.println(RandomStringUtils.randomAscii(10));               // ASCII字符
    }

    // ==================== DateUtils / DateFormatUtils ====================

    @Test
    void testDateUtils() throws ParseException {
        Date now = new Date();

        // 格式化日期
        System.out.println(DateFormatUtils.format(now, "yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.format(now));

        // 解析日期
        Date parsed = DateUtils.parseDate("2024-01-15 10:30:00", "yyyy-MM-dd HH:mm:ss");
        System.out.println(parsed);

        // 日期加减
        Date tomorrow = DateUtils.addDays(now, 1);
        Date lastMonth = DateUtils.addMonths(now, -1);
        System.out.println("明天: " + DateFormatUtils.format(tomorrow, "yyyy-MM-dd"));
        System.out.println("上月: " + DateFormatUtils.format(lastMonth, "yyyy-MM-dd"));

        // 截断到指定精度
        Date truncated = DateUtils.truncate(now, java.util.Calendar.DAY_OF_MONTH);
        System.out.println("当天零点: " + DateFormatUtils.format(truncated, "yyyy-MM-dd HH:mm:ss"));

        // 判断是否同一天
        Date date1 = DateUtils.parseDate("2024-01-15", "yyyy-MM-dd");
        Date date2 = DateUtils.parseDate("2024-01-15 12:00:00", "yyyy-MM-dd HH:mm:ss");
        System.out.println(DateUtils.isSameDay(date1, date2)); // true
    }

    // ==================== StopWatch ====================

    @Test
    void testStopWatch() throws InterruptedException {
        StopWatch stopWatch = new StopWatch();

        stopWatch.start();
        Thread.sleep(100);
        stopWatch.stop();
        System.out.println("耗时: " + stopWatch.getTime() + " ms");

        // 重置后可重新使用
        stopWatch.reset();
        stopWatch.start();
        Thread.sleep(50);
        stopWatch.stop();
        System.out.println("重置后耗时: " + stopWatch.getTime() + " ms");
    }

    // ==================== SystemUtils ====================

    @Test
    void testSystemUtils() {
        // 获取系统信息，无需判断操作系统类型
        System.out.println("Java版本: " + SystemUtils.JAVA_VERSION);
        System.out.println("Java Home: " + SystemUtils.JAVA_HOME);
        System.out.println("操作系统: " + SystemUtils.OS_NAME);
        System.out.println("是否Windows: " + SystemUtils.IS_OS_WINDOWS);
        System.out.println("是否Linux: " + SystemUtils.IS_OS_LINUX);
        System.out.println("是否Mac: " + SystemUtils.IS_OS_MAC);
        System.out.println("用户目录: " + SystemUtils.USER_HOME);
        System.out.println("临时目录: " + SystemUtils.JAVA_IO_TMPDIR);
        System.out.println("文件编码: " + SystemUtils.FILE_ENCODING);
    }
}
