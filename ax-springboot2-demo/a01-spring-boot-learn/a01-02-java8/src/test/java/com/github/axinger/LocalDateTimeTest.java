package com.github.axinger;

import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.stream.Collectors;

public class LocalDateTimeTest {
    @Test
    void test_0() {
        final LocalDateTime dateTime = LocalDateTime.now();

        List<DateModel> list = new ArrayList<>();

        final int hour = dateTime.getHour();

        for (int i = 0; i <= hour; i++) {
            final LocalDateTime localDateTime = dateTime.withMinute(0).withSecond(0).withNano(0).plusHours(-i);
//            System.out.println("localDateTime.getHour() = " + localDateTime.getHour());

            DateModel model = new DateModel();
            model.setDateTime(localDateTime);
            model.setName(String.valueOf(hour - i));
            list.add(model);
            final String format = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
//            System.out.println("format = " + format);
        }


        for (int i = 0; i <= hour; i = i + 2) {
            final LocalDateTime localDateTime = dateTime.withMinute(0).withSecond(0).withNano(0).plusHours(-i);
            final String format = localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            System.out.println("format = " + format);
        }

        // 时间分组
        final Map<Integer, List<DateModel>> collect = list.parallelStream()
                .collect(Collectors.groupingBy(o -> o.getDateTime().getHour() / 2));
        System.out.println("时间分组 collect = " + collect);

        final List<Integer> collect1 = collect.values().parallelStream()
                .map(o -> o.size())
                .collect(Collectors.toList());
        System.out.println("collect1 = " + collect1);

    }

    @Test
    void test_LocalDateTime_mix() {

//        System.out.println("LocalDateTime.now().with(LocalDate.MIN) = " + LocalDateTime.now().with(LocalTime.MIN));

//        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0,0));
//        System.out.println("start = " + start);


//        final TemporalAccessor parse = DateTimeFormatter.ofPattern("yyyy-MM-dddTHH:mm:ss.SSS").parse("2022-05-19T00:00:00.000");


//        DateTimeFormatter dtf4 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
//        LocalDateTime localDateTime2 = LocalDateTime.parse("2022-05-19 00:00:00:000", dtf4);
//
//
//        System.out.println("localDateTime2 = " + localDateTime2);

//        LocalDateTime localDateTime = LocalDateTime.now();
//        System.out.println(localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm:ss.SSS")));

//        final LocalDateTime parse = LocalDateTime.parse("2019-09-20T12:36:39.359", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
//        System.out.println("parse = " + parse);
        final String format = LocalDateTime.now().with(LocalTime.MIN).format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"));
        System.out.println("format = " + format);

        // 当天零点
        LocalDateTime.of(LocalDate.now(), LocalTime.MIN).toEpochSecond(OffsetDateTime.now().getOffset());

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSSSSSSSS");

        System.out.println(LocalDateTime.now().format(formatter));

        LocalDateTime when =
                LocalDateTime.of(2022, 5, 1, 0, 0, 0, 0);

        System.out.println(when);
    }


    @Test
    public void test1() {

        System.out.println("System.currentTimeMillis() = " + System.currentTimeMillis());

        /**
         * java.util.Date 类
         * 1. 两个构造器
         * 2.两个方法 toString,getTime()
         * */

        Date date = new Date();
        System.out.println("date = " + date);
        System.out.println("date.getTime() = " + date.getTime());
        System.out.println("new Date(date.getTime()) = " + new Date(date.getTime()));

    }

    @Test
    public void test2() {
        // 和数据库交互时候使用
        java.sql.Date date = new java.sql.Date(1638016018479L);

        System.out.println("date = " + date);

    }

    /**
     * 字符串转date
     *
     * @throws ParseException
     */
    @Test
    public void test3() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
        System.out.println("format.format(new Date()) = " + format.format(new Date()));

        String str = "2021-11-27 20:59:25:545";

        Date date = format.parse(str);
        System.out.println("字符串转date = " + date);

        String str1 = "2021-11-27T20:59:25.545";
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime time1 = LocalDateTime.parse(str1, formatter);
        System.out.println("formatter.format(LocalDateTime.now()) = " + formatter.format(LocalDateTime.now()));
        System.out.println("字符串转 time1 = " + time1);
    }

    @Test
    public void test4() {

        Calendar calendar = Calendar.getInstance();
        System.out.println("calendar.getClass() = " + calendar.getClass());
        System.out.println("calendar.get = " + calendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, 27);
        System.out.println("calendar.get = " + calendar.get(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.DAY_OF_MONTH, -5);
        System.out.println("calendar.get = " + calendar.get(Calendar.DAY_OF_MONTH));

        Date date = calendar.getTime();
        System.out.println("date = " + date);

        calendar.setTime(date);


    }

    @Test
    public void test1_1() {

        LocalDate localDate = LocalDate.now();
        System.out.println("localDate = " + localDate);


        LocalTime localTime = LocalTime.now();
        System.out.println("localTime = " + localTime);
        System.out.println("localTime = " + localTime.withSecond(3));

        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println("localDateTime = " + localDateTime);

        LocalDateTime localDateTime1 = LocalDateTime.of(2021, 11, 13, 12, 20, 01);
        System.out.println("localDateTime1 = " + localDateTime1);

        System.out.println("getDayOfMonth = " + localDateTime1.getDayOfMonth());
        System.out.println("getMonth = " + localDateTime1.getMonth());
        System.out.println("getMonthValue = " + localDateTime1.getMonthValue());
        System.out.println("getDayOfWeek = " + localDateTime1.getDayOfWeek());
        System.out.println("withDayOfMonth = " + localDateTime1.withDayOfMonth(12));


    }

    @Test
    public void test2_2() {
        Instant instant = Instant.now();
        System.out.println("instant = " + instant);// 零时区
        OffsetDateTime offsetDateTime = instant.atOffset(ZoneOffset.ofHours(8));
        System.out.println("offsetDateTime = " + offsetDateTime);

        long milli = instant.toEpochMilli();// 自1970年毫秒数
        System.out.println("milli = " + milli);

    }

    @Test
    public void test_LocalDateTime_Inhtant() {


        // 当前系统时区
        ZoneId currentZone = ZoneId.systemDefault();
        System.out.println("currentZone = " + currentZone);
        // 新时区
//        ZoneId newZone = ZoneId.of(zoneIdEnum.getZoneIdName());
//        // 时区转换
//         localDateTime.atZone(currentZone).withZoneSameInstant(newZone).toLocalDateTime();

        // UTC
        System.out.println("ZoneId.ofOffset = " + ZoneId.ofOffset("UTC", ZoneOffset.ofHours(8)));
        System.out.println("上海时间1 = " + LocalDateTime.now(ZoneOffset.ofHours(8)));

        System.out.println("上海时间2 = " + LocalDateTime.now(ZoneId.systemDefault()));

        System.out.println("LocalDateTime.now() = " + LocalDateTime.now());
        System.out.println("LocalDateTime.now() = " + LocalDateTime.now().atOffset(ZoneOffset.ofHours(8)));

        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        final LocalDateTime yyyy = LocalDateTime.parse("3000-01-01 00:00:00", df);

        System.out.println("yyyy = " + yyyy);

        final OffsetDateTime offsetDateTime1 = yyyy.atOffset(ZoneOffset.ofHours(8));
        System.out.println("offsetDateTime1 = " + offsetDateTime1);

        final Instant instant1 = yyyy.toInstant(ZoneOffset.ofHours(8));
        System.out.println("instant1 = " + instant1.getEpochSecond());

    }

    @Test
    public void test3_3() {


        System.out.println("LocalDateTime.now() = " + LocalDateTime.now().withNano(3 * 100000000));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");
        String format = formatter.format(LocalDateTime.now());
        System.out.println("format = " + format);

        DateTimeFormatter formatter1 = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
        String s1 = formatter1.format(LocalDateTime.now());
        System.out.println("s1 = " + s1);

        final String format1 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println("format1 = " + format1);

        final LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(System.currentTimeMillis()), ZoneId.systemDefault());
        System.out.println("dateTime = " + dateTime);

        System.out.println("dateTime.getDayOfWeek() = " + LocalDateTime.now().plusDays(-1).getDayOfWeek().getValue());

        System.out.println("LocalDateTime.now() = " + LocalDateTime.now().withDayOfMonth(1));


        System.out.println("0点 = " + LocalDateTime.of(LocalDate.now(), LocalTime.MIN));
        System.out.println("23点 = " + LocalDateTime.of(LocalDate.now(), LocalTime.MAX).withNano(0));

    }

    @Test
    void data_json() {

        Map map = new HashMap();
        map.put("createDate", LocalDateTime.now());


        final Map<String, Object> map2 = JSON.parseObject(JSON.toJSONString(map), Map.class);

        System.out.println("map2 = " + map2);

        LocalDateTime localDateTime = LocalDateTime.parse((String) map2.get("createDate"), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        System.out.println("localDateTime = " + localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli());


        LocalDateTime localDateTime2 = LocalDateTime.parse("2022-05-21T17:45:24.381", DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        System.out.println("localDateTime = " + localDateTime2.toInstant(ZoneOffset.of("+8")).toEpochMilli());


    }
}
