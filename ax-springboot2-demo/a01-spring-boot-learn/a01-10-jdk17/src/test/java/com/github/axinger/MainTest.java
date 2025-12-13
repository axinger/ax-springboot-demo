package com.github.axinger;

import com.alibaba.fastjson2.JSON;
import com.axing.common.util.json.JsonUtil;
import com.github.axinger.model.Person;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <a href="https://blog.csdn.net/qq_43842093/article/details/124547345?share_token=C924842A-814A-470D-AB13-B89FD09A5A60&tt_from=weixin&utm_source=weixin&utm_medium=toutiao_ios&utm_campaign=client_share&wxshare_count=1">...</a>
 */
class MainTest {

    public static void main(String[] args) {
//        FiberScope.scope().schedule(() -> {
        // 使用HttpClient抓取网站数据
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://example.com"))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(body -> {
                    // 在Fiber中处理抓取到的数据
                    processAndDisplayData(body);
                })
                .join();
//        });
    }

    private static void processAndDisplayData(String data) {
        // 处理和展示数据
        System.out.println("Fiber Thread: " + Thread.currentThread().getName());
        System.out.println("Data: " + data);
    }

    @Test
    void test() {

        // var 关键字
        var valMap = new HashMap<String, Integer>();


        Person person = new Person();
        person.setName("jim");
        System.out.println("person = " + person);

        List<Integer> integers = List.of(1, 2, 3);
        System.out.println("integers = " + integers);

        Map<String, Integer> map = Map.of("a", 1, "b", 2, "c", 3, "d", 4);
        System.out.println("map = " + map);

        List<String> list = List.of("1");
//        list.add("3");
        System.out.println("list = " + list);

    }

    /**
     * 在这之后，switch中就多了一个关键字用于跳出switch块了，那就是yield，他用于返回一个值。
     * <p>
     * 和return的区别在于：return会直接跳出当前循环或者方法，而yield只会跳出当前switch块。
     */
    @Test
    void test_switch1() {

        System.out.println("switch1() = " + switch1());
    }

    String switch1() {
        for (int i = 0; i < 3; i++) {
            System.out.println("i = " + i);
            if (i == 1) {
                return "1";
            }
            return "default";
        }
        return "a";
    }

    @Test
    void test_switch() {

        switch_2();

    }

    // switch中就多了一个关键字用于跳出switch块了，那就是yield
    void switch_2() {

        for (int i = 0; i < 3; i++) {
            System.out.println("i = " + i);
            int x = switch (i) {
                case 1 -> 1;
                case 2 -> 2;
                default -> {
//                    int len = args[1].length();
                    System.out.println("yield===");
                    yield -1;
                }
            };
            System.out.println("x = " + x);
        }
    }

    /**
     * Java 13中提供了一个Text Blocks的预览特性，并且在Java 14中提供了第二个版本的预览。
     * 使用“”“作为文本块的开始符和结束符，在其中就可以放置多行的字符串，不需要进行任何转义。看起来就十分清爽了。
     */
    @SneakyThrows
    @Test
    void test_TextBlocks() {
        String query =
                """
                        SELECT `EMP_ID`, `LAST_NAME` FROM `EMPLOYEE_TB`
                        WHERE `CITY` = 'INDIANAPOLIS'
                        ORDER BY `EMP_ID`, `LAST_NAME`;
                        """;
        System.out.println(query);


        String jsonStr = """
                {"name":"jim",
                "age":10
                }
                """;
        Person person11 = JSON.parseObject(jsonStr, Person.class);
        System.out.println(person11);
        System.out.println(person11.getName());

//        将字符串转换为对象：

        Person sutdent = JsonUtil.toBean(jsonStr, Person.class);
        System.out.println("sutdent = " + sutdent);


//        将对象转换为字符串：String jsonString = mapper.writeValueAsString(student);


        // 解析 record
        record Person1(String name, int age) {
        }
        Person1 person112 = JSON.parseObject(jsonStr, Person1.class);
        System.out.println("person112 = " + person112);

    }

    /**
     * Records
     * <p>
     * Java 14 中便包含了一个新特性：EP 359: Records，
     * <p>
     * Records的目标是扩展Java语言语法，Records为声明类提供了一种紧凑的语法，用于创建一种类中是“字段，只是字段，除了字段什么都没有”的类。
     * <p>
     * 通过对类做这样的声明，编译器可以通过自动创建所有方法并让所有字段参与hashCode()等方法。这是JDK 14中的一个预览特性。
     * <p>
     * 使用record关键字可以定义一个记录
     * <p>
     * record 解决了使用类作为数据包装器的一个常见问题。纯数据类从几行代码显著地简化为一行代码。
     * （详见： Java 14 发布了，不使用”class”也能定义类了？还顺手要干掉Lombok！ ）
     */
    @Test
    void test_record() {
        record Person1(String name, int age) {
        }

        Person1 person1 = new Person1("tom", 10);
        System.out.println(person1.name);
    }

    /// jdk17 预览模式,不是TLS
//    static String formatterPatternSwitch (Object o){
//        return switch (o) {
//            case Integer
//                    i -> String.format("int %d", i);
//            case Long
//                    l -> String.format("long %d", l);
//            case Double
//                    d -> String.format("double %f", d);
//            case String
//                    s -> String.format("String %s", s);
//            default        ->o.toString();
//        } ;
//    }

    /**
     * 封闭类
     * <p>
     * 在Java 15之前，Java认为"代码重用"始终是一个终极目标，所以，一个类和接口都可以被任意的类实现或继承。
     */

    @Test
    void test_instanceof() {

//        Object obj = "abc";
        Object obj = Arrays.asList(1, 2);
        instanceofFunc(obj);
    }

    void instanceofFunc(Object obj) {
        if (obj instanceof String str) {
            System.out.println("str.length() = " + str.length());
        } else if (obj instanceof List<?> list) {
            System.out.println("list.size() = " + list.size());
        } else {
            System.out.println("else = " + obj);
        }
    }

    @Test
    void test10() {

    }

    @Test
    public void test21() {

    }

}
