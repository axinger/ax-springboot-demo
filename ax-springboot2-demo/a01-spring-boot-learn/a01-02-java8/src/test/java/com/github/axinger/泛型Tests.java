package com.github.axinger;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;
import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"all"})
public class 泛型Tests {

    public static <T> T testE(String text, TypeToken<T> typeToken) {
        Type type = typeToken.getType();
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Class<?> rawType = (Class<?>) pType.getRawType();
            Type[] typeArgs = pType.getActualTypeArguments();

            // 处理 Map 类型
            if (rawType == Map.class) {
                return handleMap(text, typeArgs);
            }
            // 处理 List 类型
            else if (rawType == List.class) {
                return handleList(text, typeArgs[0]);
            }
            // 其他集合或自定义类型可在此扩展
        }
        // 处理非泛型类型（如直接返回 String、Integer 等）
        else if (type instanceof Class) {
            Class<?> clazz = (Class<?>) type;
            return (T) convertBasicType(text, clazz);
        }
        throw new IllegalArgumentException("Unsupported type: " + type);
    }

    // 处理 Map 类型
    private static <T> T handleMap(String text, Type[] typeArgs) {
        try {
            // 假设固定键为 "key"，值根据泛型参数转换
            Map<Object, Object> map = new HashMap<>();
            Object key = convertBasicType("key", (Class<?>) typeArgs[0]); // 键类型转换
            Object value = convertBasicType(text, (Class<?>) typeArgs[1]); // 值类型转换
            map.put(key, value);
            return (T) map;
        } catch (Exception e) {
            throw new IllegalArgumentException("Map conversion failed: " + e.getMessage());
        }
    }

    // 处理 List 类型
    private static <T> T handleList(String text, Type elementType) {
        try {
            List<Object> list = new ArrayList<>();
            Object element = convertBasicType(text, (Class<?>) elementType); // 元素类型转换
            list.add(element);
            return (T) list;
        } catch (Exception e) {
            throw new IllegalArgumentException("List conversion failed: " + e.getMessage());
        }
    }

    // 基本类型转换工具方法
    private static <T> T convertBasicType(String text, Class<T> targetType) {
        if (targetType == String.class) {
            return targetType.cast(text);
        } else if (targetType == Integer.class || targetType == int.class) {
            return targetType.cast(Integer.parseInt(text));
        } else if (targetType == Long.class || targetType == long.class) {
            return targetType.cast(Long.parseLong(text));
        } else if (targetType == Boolean.class || targetType == boolean.class) {
            return targetType.cast(Boolean.parseBoolean(text));
        } else {
            throw new UnsupportedOperationException("Unsupported type: " + targetType);
        }
    }

    public <T> T testA(T t) {

        return t;
    }

    public <T> T testB(Class<T> t) {

        return null;
    }

    public <T> T testC(Type t) {

        return null;
    }

    public <T> T testD(Type t) {

        return null;
    }

    @Test
    public void test1() {
        String test = testA("123");

        Long test1 = testA(1L);

        Integer i = testB(Integer.class);
        String str = testB(String.class);

//        List<String> list = testC(Map<String,String>.class);

        JSONObject.parseObject("", new TypeReference<List<String>>() {
        });

        JSONObject.parseObject("", Type.class, null);

        TypeToken<Map<String, String>> typeToken = new TypeToken<Map<String, String>>() {
        };
        Type type = typeToken.getType();
    }

    // 测试用例
    @Test
    public void test44() {
        // 测试 Map<String, String>
        Map<String, String> map1 = testE("value", new TypeToken<Map<String, String>>() {
        });
        System.out.println("Map<String, String>: " + map1); // 输出: {key=value}

        // 测试 Map<Integer, Integer>
        Map<Integer, Integer> map2 = testE("123", new TypeToken<Map<Integer, Integer>>() {
        });
        System.out.println("Map<Integer, Integer>: " + map2); // 输出: {123=123}

        // 测试 List<String>
        List<String> list1 = testE("hello", new TypeToken<List<String>>() {
        });
        System.out.println("List<String>: " + list1); // 输出: [hello]

        // 测试 List<Integer>
        List<Integer> list2 = testE("456", new TypeToken<List<Integer>>() {
        });
        System.out.println("List<Integer>: " + list2); // 输出: [456]

        // 测试直接返回 Integer
        Integer num = testE("789", new TypeToken<Integer>() {
        });
        System.out.println("Integer: " + num); // 输出: 789
    }
}

