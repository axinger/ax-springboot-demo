package com.github.axinger;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONPath;
import org.junit.jupiter.api.Test;

import java.util.List;

//https://alibaba.github.io/fastjson2/jsonpath_cn
public class FastJson2JSONPathTest {

    /*
     @ 表示当前对象。
     [?age=10] 是一个过滤条件，表示选择 age 属性等于 10 的对象。
     因此，@[?age=10] 的含义是：在当前对象的集合中，选择所有 age 属性等于 10 的对象。


     $.age:
    $ 表示根对象。
    $.age 表示根对象中的 age 属性。
    这种语法通常用于 JSON 对象或 JSON 数组的根级别。
    @.age:
    @ 表示当前对象。
    @.age 表示当前对象中的 age 属性。
    这种语法在某些情况下更明确地指定了当前对象。
      */
    @Test
    void test01() {

        String str = """
                {
                    "store": {
                        "book": [
                            {
                                "category": "reference",
                                "author": "Nigel Rees",
                                "title": "Sayings of the Century",
                                "price": 8.95
                            },
                            {
                                "category": "fiction",
                                "author": "Evelyn Waugh",
                                "title": "Sword of Honour",
                                "price": 12.99
                            },
                            {
                                "category": "fiction",
                                "author": "Herman Melville",
                                "title": "Moby Dick",
                                "isbn": "0-553-21311-3",
                                "price": 8.99
                            },
                            {
                                "category": "fiction",
                                "author": "J. R. R. Tolkien",
                                "title": "The Lord of the Rings",
                                "isbn": "0-395-19395-8",
                                "price": 22.99
                            }
                        ],
                        "bicycle": {
                            "color": "red",
                            "price": 19.95
                        }
                    },
                    "expensive": 10
                }
                """;


        JSONObject jsonObject = JSON.parseObject(str);

        // 1 获取单个节点的值
        Object color = JSONPath.eval(jsonObject, "$.store.bicycle.color");
        System.out.println("Bicycle color: " + color);

        // 2 获取数组中的元素
        // 获取 store.book 数组中的第一个元素
        Object firstBook = JSONPath.eval(jsonObject, "$.store.book[0]");
        System.out.println("First book: " + firstBook);

        //3 过滤数组元素
        // 获取价格低于 expensive 值的书籍
//        Object cheapBooks = JSONPath.eval(jsonObject, "$.store.book[?(@.price < $.expensive)]");
        Object cheapBooks = JSONPath.eval(jsonObject, "$.store.book[?(@.price < 10)]");
        System.out.println("Cheap books: " + cheapBooks);

        //4 递归查找
        //使用递归通配符 .. 可以递归查找所有匹配的节点。例如，查找所有 author 字段的值
        // 查找所有 author 字段的值
        Object allAuthors = JSONPath.eval(jsonObject, "$..author");
        System.out.println("All authors: " + allAuthors);

        //3. 修改 JSON 数据
        // 修改 store.bicycle.color 的值为 blue
        JSONPath.set(jsonObject, "$.store.bicycle.color", "blue");
        System.out.println("Modified JSON: " + jsonObject.toJSONString());


    }

    /*
        @ 表示当前对象。
        [?age=10] 是一个过滤条件，表示选择 age 属性等于 10 的对象。
        因此，@[?age=10] 的含义是：在当前对象的集合中，选择所有 age 属性等于 10 的对象。


        $.age:
        $ 表示根对象。
        $.age 表示根对象中的 age 属性。
        这种语法通常用于 JSON 对象或 JSON 数组的根级别。
        @.age:
        @ 表示当前对象。
        @.age 表示当前对象中的 age 属性。
        这种语法在某些情况下更明确地指定了当前对象。

        =================================================
           分析
        根对象设置:
        JSONPath.set(root, "$.age", 100);
        $.age 设置根对象的 age 属性为 100。
        JSONPath.set(root, "@.age", 200);
        @.age 也设置根对象的 age 属性为 200。
        效果相同：因为 root 是根对象，$ 和 @ 在这里指向同一个对象。
        嵌套对象设置:
        JSONPath.set(root, "$.nested.age", 200);
        $.nested.age 设置 nested 对象的 age 属性为 200。
        JSONPath.set(root, "@.nested.age", 300);
        @.nested.age 也设置 nested 对象的 age 属性为 300。
        效果相同：因为 nested 是 root 的一个属性，$ 和 @ 在这里指向同一个对象。
        数组元素设置:
        JSONPath.set(root, "$.list[0].age", 300);
        $.list[0].age 设置 list 数组中第一个元素的 age 属性为 300。
        JSONPath.set(root, "@.list[0].age", 400);
        @.list[0].age 也设置 list 数组中第一个元素的 age 属性为 400。
        效果相同：因为 list 是 root 的一个属性，$ 和 @ 在这里指向同一个对象。

        https://alibaba.github.io/fastjson2/jsonpath_cn
 */
    @Test
    void test02() {

        String str = """
                {
                    "id":101,
                    "name": "张三",
                    "age": 10,
                    "books": [
                        {
                            "id":1,
                            "name": "西游记",
                            "age": 10
                        },
                        {
                            "id":2,
                            "name": "水浒传",
                            "age": 11
                        },
                        {
                            "id":3,
                            "name": "三国演义",
                            "age": 11
                        },
                        {
                            "id":4,
                            "name": "红楼梦",
                            "age": 12
                        }
                    ],
                    "dog": {
                        "name": "哈士奇",
                        "age": 10
                    }
                }
                """;
        JSONObject root = JSON.parseObject(str);
        System.out.println("root = " + JSON.toJSONString(root));

        System.out.println("取值================================================");
        System.out.println("$.dog.name==" + JSONPath.eval(root, "$.dog.name"));

        System.out.println("数组中指定值,返回集合===" + JSONPath.eval(root, "$.books.name"));


        try {
            System.out.println("取值中间值不存在的对象==" + JSONPath.eval(root, "$.cat.name")); //取值中间值不存在的对象,不报错
        } catch (Exception e) {
            System.out.println("取值不存在的对象");
        }

        try {
            System.out.println("取值list越界====" + JSONPath.eval(root, "$.books[3].name")); //取值中间值不存在的对象,不报错
        } catch (Exception e) {
            System.out.println("取值list越界");
        }

        Object eval = JSONPath.eval(root, "$.books[0,2]");
        System.out.println("数组选择多个 = " + eval);

        System.out.println("数组选择,范围 = " + JSONPath.eval(root, "$.books[0:2]"));

        System.out.println("数组选择,in范围 = " + JSONPath.eval(root, "$.books[?(@.id in (2,3))]"));

        System.out.println("数组选择,值等于 = " + JSONPath.eval(root, "$.books[?(@.id = 2 )]"));
        System.out.println("数组选择,age = 11值等于返回list = " + JSONPath.eval(root, "$.books[?(@.age = 11 )]"));
        System.out.println("数组选择,age = 11值等于返回list,取值0,取值不到就是null = " + JSONPath.eval(root, "$.books[?(@.age = 15 )][11]"));
        List<Object> books = (List<Object>) JSONPath.eval(root, "$.books");
        for (int i = 0; i < books.size(); i++) {
            JSONObject book = (JSONObject) books.get(i);
            if (book.getIntValue("age") == 11) {
                System.out.println("找到符合条件的元素下标：" + i);
            }
        }

        System.out.println("所有的值,深度查找 = " + JSONPath.eval(root, "$..id"));

        System.out.println("过滤================================================");
        System.out.println("根路径==" + JSONPath.eval(root, "[?(@.age > 10)]"));
        System.out.println("根路径==" + JSONPath.eval(root, "[?(@.age = 10)]"));

        // 指定对象过滤
        System.out.println("根路径dog==" + JSONPath.eval(root, "$.dog[?(@.age > 10)]"));
        System.out.println("根路径dog==" + JSONPath.eval(root, "$.dog[?(@.age = 10)]"));

        // 获取 books 中 age > 10 的书籍名称  .name
        Object result = JSONPath.eval(root, "$.books[?(@.age > 10)].name"); //这个方式是字符串比较
        System.out.println("符合条件的书籍名,返回指定的集合：" + result);


//        Object result2 = JSONPath.eval(root, "$.books[age > 10]");
//        System.out.println("符合条件的书籍名,返回指定的集合2：" + result2);

        // 判断 dog 的 age 是否大于 10
//
//        Object isGreaterThan10 = JSONPath.eval(root, "$.dog.(@age>10)");

//        //Object eval3 = JSONPath.eval(map, "@[?(@.age=10)]");
//        Object age等于值 = JSONPath.eval(root, "$.dog.(@.age=10)");
//        System.out.println("dog.age > 10 ? " + age等于值);


        System.out.println("修改值================================================");
        // 使用 $.age 和 @.age 进行设置和评估
        System.out.println("使用 $.age 和 @.age,修改值,效果一样");
        JSONPath.set(root, "$.age", 100);
        System.out.println("After setting $.age: " + root);

        JSONPath.set(root, "@.age", 200);
        System.out.println("After setting @.age: " + root);

        System.out.println("================================================");

        JSONPath.set(root, "$.dog.name", "柯基");
        JSONPath.set(root, "$.dog.age", 3); // 修改不存在的值,成功
        System.out.println("After setting $.dog.name: " + root);
        System.out.println("================================================");

        JSONPath.set(root, "@.dog.name", "柴犬");
        JSONPath.set(root, "@.dog.age", 4); // 修改不存在的值,成功
        System.out.println("After setting @.nested.age: " + root);

        System.out.println("================================================");
        JSONObject cat = new JSONObject();
        cat.put("id", 1);
        cat.put("name", "加菲猫");
        JSONPath.set(root, "$.cat", cat);
        System.out.println("添加一个对象: " + root);

        System.out.println("================================================");
        JSONObject book5 = new JSONObject();
        book5.put("id", 5);
        book5.put("name", "金瓶梅");
        JSONPath.of("$.books").arrayAdd(root, book5);
        System.out.println("数组添加一个对象: " + root);

        System.out.println("================================================");
        JSONPath.set(root, "$.books[0].price", 10.00);
        System.out.println("After setting $.books[0].price: " + root);

        JSONPath.set(root, "@.books[1].price", 12.01);
        System.out.println("After setting @.books[1].price: " + root);

        try {
            JSONPath.set(root, "@.books[3].price", 12.01); //越界
            System.out.println("越界 @.books[3].price: " + root);
        } catch (Exception e) {
            System.out.println("不能list越界");
        }

    }

}
