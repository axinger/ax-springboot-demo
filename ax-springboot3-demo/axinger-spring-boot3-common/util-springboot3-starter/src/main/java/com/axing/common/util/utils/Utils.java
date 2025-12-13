package com.axing.common.util.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utils {

    /**
     * 将时间日期字符串转换成cron表达式
     *
     * @param dateStr
     * @return
     */
    public static String dateStrConvertCronExpression(String dateStr, Integer minute) {
        DateTimeFormatter localDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime date = LocalDateTime.parse(dateStr, localDateTimeFormat);

        if (minute != null) {
            date = date.plusMinutes(minute);
        }

        String sb = date.getSecond() + " " + date.getMinute() + " " + date.getHour() + " " +
                date.getDayOfMonth() + " " + date.getMonthValue() + " " + "?";

        return sb;
    }
}
