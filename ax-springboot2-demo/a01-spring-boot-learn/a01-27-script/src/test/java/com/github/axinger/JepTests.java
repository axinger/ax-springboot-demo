//package com.github.axinger;
//
//import jep.Interpreter;
//import jep.JepConfig;
//import jep.JepException;
//import jep.SharedInterpreter;
//import org.junit.jupiter.api.Test;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
//public class JepTests {
//
//    @Test
//    public void test01() {
//
//        JepConfig config = new JepConfig();
//        try (Interpreter jep = new SharedInterpreter()) {
//
//            // 准备数据
//            Map<String, Object> data = new HashMap<>();
//            data.put("name", "李四");
//            data.put("age", 30);
//            data.put("hobbies", Arrays.asList("阅读", "游泳", "编程"));
//
//            // 直接设置变量，无需 JSON 序列化
//            jep.set("user_data", data);
//
//            // 执行 Python 3 代码
//            jep.exec(
//                    "def process_data(data):\n" +
//                            "    result = {}\n" +
//                            "    result['greeting'] = f'Hello, {data[\\\"name\\\"]}!'\n" +
//                            "    result['age_after_10'] = data['age'] + 10\n" +
//                            "    result['hobbies'] = [hobby.upper() for hobby in data['hobbies']]\n" +
//                            "    return result\n" +
//                            "\n" +
//                            "result = process_data(user_data)\n" +
//                            "print('Python 处理结果:', result)"
//            );
//
//            // 获取结果
//            Object result = jep.getValue("result");
//            System.out.println("Java 接收结果: " + result);
//
//        } catch (JepException e) {
//            e.printStackTrace();
//        }
//    }
//}
