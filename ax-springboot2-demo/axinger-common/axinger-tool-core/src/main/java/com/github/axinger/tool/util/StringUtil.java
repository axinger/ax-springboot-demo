package com.github.axinger.tool.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    /// 占位符格式化方法,未匹配的:姓名=jim,年龄=20,性别={},保留占位符
    public static String format(CharSequence template, Object... params) {
        // 使用正则匹配所有 {} 形式的占位符
        Pattern pattern = Pattern.compile("\\{}");
        Matcher matcher = pattern.matcher(template);
        StringBuilder result = new StringBuilder();
        int index = 0;
        while (matcher.find()) {
            if (index >= params.length) {
                break;
            }
            Object value = params[index++];
            if (value == null) {
                continue;
            }
            matcher.appendReplacement(result, Matcher.quoteReplacement(value.toString()));
        }
        matcher.appendTail(result);
        return result.toString();
    }

    /// 格式化字符串, ,未匹配的:姓名=jim,年龄=20,性别={sex},保留占位符
    public static String format(CharSequence template, Map<String, Object> paramsMap) {
        // 使用正则匹配所有 {key} 形式的占位符
        // \\{ 和 \\}：匹配占位符的开头 { 和结尾 }（因为 { 和 } 在正则表达式中有特殊含义，所以需要用 \\ 转义）。
        //(.+?)：这是一个捕获组，用来匹配 {} 中的任意内容（即 key），.+? 表示非贪婪匹配，即尽可能少地匹配字符。
        Pattern pattern = Pattern.compile("\\{(.+?)}");
        Matcher matcher = pattern.matcher(template);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            // 提取占位符内的key
            String key = matcher.group(1);
            // 获取值
            Object value = paramsMap.get(key);
            if (value == null) {
                continue;
            }
            // 替换当前匹配项
            matcher.appendReplacement(result, value.toString());
        }
        // 添加剩余未匹配部分
        matcher.appendTail(result);
        return result.toString();
    }


    /**
     * 将时间日期字符串转换成cron表达式
     *
     * @param dateStr
     * @return
     */
    public static String toCronExpression(String dateStr, Integer minute) {
        DateTimeFormatter localDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse(dateStr, localDateTimeFormat);
        if (minute != null) {
            date = date.plusMinutes(minute);
        }
        return date.getSecond() + " " + date.getMinute() + " " + date.getHour() + " " + date.getDayOfMonth() + " " + date.getMonthValue() + " " + "?";
    }

}
