package com.github.axinger;

import com.google.common.reflect.TypeToken;
import org.junit.jupiter.api.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenericTest {

    public static <T> T testE(String text, TypeToken<T> typeToken) {
        Type type = typeToken.getType();
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Class<?> rawType = (Class<?>) pType.getRawType();
            Type[] typeArgs = pType.getActualTypeArguments();

            if (rawType == Map.class) {
                return handleMap(text, typeArgs);
            } else if (rawType == List.class) {
                return handleList(text, typeArgs[0]);
            }
        } else if (type instanceof Class) {
            Class<?> clazz = (Class<?>) type;
            return (T) convertBasicType(text, clazz);
        }
        throw new IllegalArgumentException("Unsupported type: " + type);
    }

    // 处理 Map 类型：使用 text 作为键和值的来源
    private static <T> T handleMap(String text, Type[] typeArgs) {
        try {
            Map<Object, Object> map = new HashMap<>();
            // 键和值均使用 text 作为输入，根据泛型类型动态转换
            Object key = convertBasicType(text, (Class<?>) typeArgs[0]);
            Object value = convertBasicType(text, (Class<?>) typeArgs[1]);
            map.put(key, value);
            return (T) map;
        } catch (Exception e) {
            throw new IllegalArgumentException("Map conversion failed: " + e.getMessage());
        }
    }

    // 处理 List 类型（保持不变）
    private static <T> T handleList(String text, Type elementType) {
        try {
            List<Object> list = new ArrayList<>();
            Object element = convertBasicType(text, (Class<?>) elementType);
            list.add(element);
            return (T) list;
        } catch (Exception e) {
            throw new IllegalArgumentException("List conversion failed: " + e.getMessage());
        }
    }

    // 增强的基本类型转换方法
    private static <T> T convertBasicType(String text, Class<T> targetType) {
        try {
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
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Cannot convert '" + text + "' to " + targetType.getSimpleName()
            );
        }
    }

    // 测试用例
    @Test
    public void test2() {
        // 测试 Map<Integer, Integer>（键和值均为 Integer）
        Map<Integer, Integer> map1 = testE("123", new TypeToken<Map<Integer, Integer>>() {
        });
        System.out.println("Map<Integer, Integer>: " + map1); // 输出: {123=123}

        // 测试 Map<String, String>（键和值均为 String）
        Map<String, String> map2 = testE("value", new TypeToken<Map<String, String>>() {
        });
        System.out.println("Map<String, String>: " + map2); // 输出: {value=value}

        // 测试 Map<Boolean, Long>（键为 Boolean，值为 Long）
//        Map<Boolean, Long> map3 = testE("true", new TypeToken<Map<Boolean, Long>>() {});
//        System.out.println("Map<Boolean, Long>: " + map3); // 输出: {true=1}（需调整逻辑）
//
//        // 测试 List<Integer>
//        List<Integer> list = testE("456", new TypeToken<List<Integer>>() {});
//        System.out.println("List<Integer>: " + list); // 输出: [456]
    }
}
