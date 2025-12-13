package com.github.axinger;

import cn.hutool.core.lang.Console;
import cn.hutool.core.lang.ConsoleTable;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.log.Log;
import cn.hutool.log.dialect.console.ConsoleLog;
import cn.hutool.log.dialect.log4j2.Log4j2Log;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import org.junit.jupiter.api.Test;

import java.util.*;

public class LogTests {

    @Test
    void test1() {
        Console.log("a={},b={}", 1, 2);
        Console.error("a={},b={}", 1, 2);
        Console.table(ConsoleTable.create()
                .addHeader("a", "b", "c")
                .addBody("1", "2", "3")
        );

        Console.print("a={},b=2", 1, 2); //不换行
        Console.printProgress('#', 3);
        Console.print("\n");
        Console.printProgress('#', 10, 0.6);
        Console.print("\n");
        Console.log(new Throwable("aa"), "Throwable:a={},b={}", 1, 2);
        Console.print("\n");
        Console.error(new Throwable("aa"), "Throwable:a={},b={}", 3, 4);

        System.out.println("Console.where() = " + Console.where());

        Console.print("请输入值===================");
        String input = Console.input();
        System.out.println("input = " + input);
    }


    @Test
    void test2() {
        Console.log("a={},b=2", 1, 2);
        Log log = new ConsoleLog(LogTests.class);
        log.debug("11111");
        log.info("2222");
    }

    @Test
    void test3() {
        Console.log("a={},b=2", 1, 2);
        Log log = new Log4j2Log(LogTests.class);
        log.debug("11111");
        log.info("2222");
    }


    @Test
    void test4() {
        Console.print("请输入值===================");

        Scanner scanner = Console.scanner();
//        System.out.println("scanner = " + scanner);

        String input = Console.input();
        System.out.println("input = " + input);
    }

    @Test
    void test5() {

        List<Person> list = new ArrayList<>();
        list.add(Person.builder()
                .id(1)
                .name("张三")
                .build());
        list.add(Person.builder()
                .id(2)
                .name("李四")
                .build());
        list.add(Person.builder()
                .id(3)
                .name("王五")
                .build());

        String jsonStr = JSONUtil.toJsonStr(list);


        List<Map<String, Object>> objects = JSONObject.parseObject(jsonStr, new TypeReference<>() {
        });

        System.out.println("objects = " + objects);
        ConsoleTable consoleTable = ConsoleTable.create();
        String[] titles = objects.getFirst().keySet().toArray(new String[0]);
        consoleTable.addHeader(titles);
        for (Map<String, Object> map : objects) {
            Object[] array = map.values().toArray(new Object[0]);
            String[] stringArray = Arrays.stream(array)
                    .map(obj -> obj == null ? "" : obj.toString())
                    .toArray(String[]::new);
            consoleTable.addBody(stringArray);
        }
        Console.table(consoleTable);


        ConsoleTable consoleTable2 = ConsoleTable.create();
        consoleTable2.addHeader(LambdaUtil.getFieldName(Person::getId), LambdaUtil.getFieldName(Person::getName));

        for (Person person : list) {
            consoleTable2.addBody(String.valueOf(person.getId()), person.getName());
        }
        Console.table(consoleTable2);
    }
}
