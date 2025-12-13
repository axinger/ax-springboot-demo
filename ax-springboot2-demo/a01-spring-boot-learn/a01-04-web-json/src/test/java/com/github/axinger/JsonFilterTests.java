package com.github.axinger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.github.axinger.entity.Department;
import com.github.axinger.entity.Employee;
import com.github.axinger.entity.Node;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

public class JsonFilterTests {

    @SneakyThrows
    @Test
    public void test1() {


        SimpleFilterProvider filterProvider = new SimpleFilterProvider()
//                .addFilter("departmentFilter", SimpleBeanPropertyFilter.filterOutAllExcept("name", "employees")) //  只序列化指定的字段
                .addFilter("departmentFilter", SimpleBeanPropertyFilter.serializeAllExcept("employees")) //除了

//                .addFilter("employeeFilter", SimpleBeanPropertyFilter.serializeAllExcept("employees.salary"))
//                .addFilter("employeeFilter", SimpleBeanPropertyFilter.filterOutAllExcept("id","name", "salary"))
                .addFilter("employeeFilter", SimpleBeanPropertyFilter.filterOutAllExcept("id", "salary"));

//        filterProvider.addFilter("employeeFilter", SimpleBeanPropertyFilter.serializeAllExcept("salary"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.setFilterProvider(filterProvider);


        Department department = new Department();
        department.setName("IT");
        department.setId("123");
        department.setEmployees(Arrays.stream(new Employee[]{
                new Employee("1", "Jack", new BigDecimal("10000")),
                new Employee("2", "Tom", new BigDecimal("20000")),
                new Employee("3", "Jerry", new BigDecimal("30000"))
        }).toList());


        String json = mapper.writeValueAsString(department);

        System.out.println("json = " + json);

//        public interface UserMixIn {
//            @JsonIgnore ContactInfo getContactInfo(); // 忽略整个嵌套对象
//            // 或者
//            @JsonIgnore String getPassword();
//        }
//        mapper.addMixIn(User.class, UserMixIn.class);

    }


    @SneakyThrows
    @Test
    public void test2_循环引用() {

        // 创建对象映射器
        ObjectMapper mapper = new ObjectMapper();

// 配置过滤器（可选，如果需要额外过滤字段）
        SimpleFilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter("circularFilter",
                        SimpleBeanPropertyFilter.serializeAllExcept() // 这里可以添加要排除的字段名
                )
                .setFailOnUnknownId(false); // 避免找不到过滤器时报错

        mapper.setFilterProvider(filterProvider);


        // 过滤掉所有节点的"name"字段
//        SimpleFilterProvider filterProvider = new SimpleFilterProvider()
//                .addFilter("circularFilter",
//                        SimpleBeanPropertyFilter.serializeAllExcept("name")
//                );

// 构建测试数据
        Node root = new Node("root");

        Node child1 = new Node("child1");
        child1.setParent(root);
        root.addChild(child1);

        Node child2 = new Node("child2");
        root.addChild(child2);

        Node parent = new Node("parent");
        root.setParent(parent);

// 序列化
        String json = mapper.writeValueAsString(root);

        System.out.println("json = " + json);

        //json = {"name":"root","children":[{"name":"child1","children":[]},{"name":"child2","children":[]}]}
        //json = {"name":"root","children":[{"name":"child1","children":[]},{"name":"child2","children":[]}]}

/*

    {
            "name": "root",
                "children": [
            {
                "name": "child1",
                    "children": []
            },
            {
                "name": "child2",
                    "children": []
            }
  ]
        }
 */

    }


}
