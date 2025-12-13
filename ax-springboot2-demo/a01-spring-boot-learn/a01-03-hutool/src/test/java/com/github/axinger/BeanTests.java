package com.github.axinger;

import cn.hutool.core.bean.BeanDesc;
import cn.hutool.core.bean.BeanPath;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNode;
import cn.hutool.core.lang.tree.TreeUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanTests {

    @Test
    void test1() {
        Map<String, List<Map<String, String>>> person = Map.of("person", List.of(Map.of("name", "jim")));


//        Object property = BeanUtil.getProperty(person, "person[0].name");
        Object property = BeanUtil.getProperty(person, "person[1].name");
        System.out.println("property = " + property);


    }

    @Test
    void test2() {
        List<Map<String, String>> list = List.of(Map.of("name", "jim"));
//        Object property = BeanUtil.getProperty(list, "[0].name");
        Object property = BeanUtil.getProperty(list, "[1].name");
        System.out.println("property = " + property);

        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);

        Object resolver = new BeanPath("[id=1]").get(map);
        System.out.println("resolver = " + resolver);

//        Object result = resolver.get(tempMap);//ID为1

    }


    @Test
    void test3() {
        // 构建node列表
        List<TreeNode<String>> nodeList = CollUtil.newArrayList();

        nodeList.add(new TreeNode<>("1", "0", "系统管理", 5));
        nodeList.add(new TreeNode<>("11", "1", "用户管理", 222222));
        nodeList.add(new TreeNode<>("111", "11", "用户添加", 0));
        nodeList.add(new TreeNode<>("2", "0", "店铺管理", 1));
        nodeList.add(new TreeNode<>("21", "2", "商品管理", 44));
        nodeList.add(new TreeNode<>("221", "2", "商品管理2", 2));

        // 0表示最顶层的id是0
//        List<Tree<String>> treeList = TreeUtil.build(nodeList, "0");
        List<Tree<String>> treeList = TreeUtil.build(nodeList, "1");
        System.out.println("treeList = " + treeList);


    }

    // 动态赋值
    @Test
    void test4() {
        Person person = Person.builder()
                .build();
        DynaBean dynaBean = DynaBean.create(person);
        dynaBean.set("name", "jim");
        System.out.println("person = " + person);
        Object name = dynaBean.get("name");
        System.out.println("name = " + name);
    }

    @Test
    void test5() {
        BeanDesc desc = BeanUtil.getBeanDesc(Person.class);
// User
        System.out.println(desc.getSimpleName());

// age
        System.out.println(desc.getField("age").getName());
// getAge
        System.out.println(desc.getGetter("age").getName());
// setAge
        System.out.println(desc.getSetter("age").getName());

        Person user = new Person();
        desc.getProp("name").setValue(user, "张三"); // 上层封装,使用就是DynaBean
        System.out.println("user = " + user);


    }

    @Test
    void test6() {
        Person person1 = new Person();
        person1.setName("jim");
        person1.setAge(22);

        Person bean = BeanUtil.toBean(person1, Person.class);
        System.out.println("bean = " + bean);

        //将bean的部分属性转换成map
        Map<String, Object> map = BeanUtil.beanToMap(bean);
        System.out.println("map = " + map);


    }
}
