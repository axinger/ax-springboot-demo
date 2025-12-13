package com.github.axinger;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.StrUtil;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CollTests {

    @Test
    public void toIdentityMapTest() {
        List<Student> list = new ArrayList<>();
        list.add(new Student(1, 1, "张三"));
        list.add(new Student(1, 2, "李四"));
        list.add(new Student(1, 3, "王五"));
        list.add(new Student(2, 1, "擎天柱"));
        list.add(new Student(2, 2, "威震天"));
        list.add(new Student(2, 2, "霸天虎"));

        // 将学生列表转换为 Map，键为学生 ID，值为学生对象, 相同的就会取最新的
        Map<Long, Student> identityMap = CollStreamUtil.toIdentityMap(list, Student::getStudentId);
        System.out.println(identityMap);
    }

    @Test
    public void toMapTest() {
        List<Student> list = new ArrayList<>();
        list.add(new Student(1, 1, "张三"));
        list.add(new Student(1, 2, "李四"));
        list.add(new Student(2, 3, "王五"));
        list.add(new Student(2, 1, "擎天柱"));
        list.add(new Student(2, 2, "威震天"));
        list.add(new Student(2, 2, "霸天虎"));
        // 将学生列表转换为 Map，自定义键为学生 ID，值为学生名称
        Map<Long, String> map = CollStreamUtil.toMap(list, Student::getStudentId, Student::getName);
        System.out.println(map);
        // 将学生列表转换为 Map，自定义键为学生 ID，值为学生名称
        Map<Long, Student> map2 = CollStreamUtil.toMap(list, Student::getStudentId, value -> value);
        System.out.println("map2 = " + map2);
    }

    @Test
    public void toListTest() {
        List<Student> list = new ArrayList<>();
        list.add(new Student(1, 1, "张三"));
        list.add(new Student(1, 2, "李四"));
        list.add(new Student(2, 3, "王五"));
        list.add(new Student(2, 1, "擎天柱"));
        list.add(new Student(2, 2, "威震天"));
        list.add(new Student(2, 2, "霸天虎"));
        // 提取所有学生信息的姓名
        List<String> nameList = CollStreamUtil.toList(list, Student::getName);
        System.out.println(nameList);
    }

    @Test
    public void groupByKeyTest() {
        List<Student> list = new ArrayList<>();
        list.add(new Student(1, 1, "张三"));
        list.add(new Student(1, 2, "李四"));
        list.add(new Student(2, 3, "王五"));
        list.add(new Student(2, 1, "擎天柱"));
        list.add(new Student(2, 2, "威震天"));
        list.add(new Student(2, 2, "霸天虎"));
        // 按班级分组
        Map<Long, List<Student>> map = CollStreamUtil.groupByKey(list, Student::getClassId);
        System.out.println(map);
    }

    @Test
    public void mergeTest() {
        Map<Integer, Student> map1 = new HashMap<>();
        map1.put(1, new Student(1, 1, "张三"));
        Map<Integer, Student> map2 = new HashMap<>();
        map2.put(2, new Student(1, 2, "李四"));


        // 合并两个Map
        Map<Integer, String> mergedMap = CollStreamUtil.merge(map1, map2, (student1, student2) -> {
            // 定义merge规则：
            if (student1 == null && student2 == null) {
                return null;
            } else if (student1 == null) {
                return student2.getName();
            } else if (student2 == null) {
                return student1.getName();
            } else {
                return StrUtil.join(",", student1.getName(), student2.getName());
            }
        });
        System.out.println(mergedMap);
    }

}
