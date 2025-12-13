package com.github.axinger;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.function.*;


@FunctionalInterface
interface MyAction {
    void run();
}


@FunctionalInterface
interface MyAction2 {
    void run(String name);
}


@FunctionalInterface
interface MyAction3 {
    String run(String name);
}

@FunctionalInterface
interface MyAction32 {
    String run(String name, Integer age);
}

@FunctionalInterface
interface MyAction33 {
    String run(String name, Integer age);

    default String run2(String name, Integer age) {

        return name + age;
    }
}


@FunctionalInterface
interface MyAction44 {

    String run(LambdaTest name, String title);

}


public class LambdaTest {

    static String actionFunc(String data) {
        System.out.println("data = " + data);
        return "aaa";
    }

    // Lambda 表达式
    @Test
    public void test() {
        MyAction myAction = () -> System.out.println("true = " + true);

        myAction.run();

        Comparator<Integer> comparator = Integer::compare;
        System.out.println(comparator.compare(10, 1));

        /// 方法引用
        Comparator<Integer> comparator2 = Integer::compare;
        System.out.println(comparator2.compare(10, 1));

    }

    /// 6种情况
    @Test
    public void test_1() {

        /// 无参,无返回值
        MyAction myAction = () -> System.out.println("无参,无返回值 : ");
        myAction.run();

        Runnable runnable = () -> System.out.println("无参,无返回值 : Runnable");
        runnable.run();
    }

    @Test
    public void test_2() {

        /// 有参,无返回值
        MyAction2 myAction2 = (name) -> System.out.println("有参,无返回值 : " + name);
        myAction2.run("AAA");

        Consumer<String> consumer = data -> System.out.println("有参,无返回值 : " + data);
        consumer.accept("Consumer");
    }

    @Test
    public void test_3() {

        /// 有参,有返回值
        MyAction3 myAction3 = (name) -> "有参,有返回值 : " + name;
        String run = myAction3.run("AAA");
        System.out.println("run = " + run);
    }

    @Test
    public void test_32() {

        /// 有多参,有返回值
        MyAction32 myAction3 = (name, age) -> {
            System.out.println("有多参,有返回值........");
            return "有参,有返回值 : " + " name = " + name + " age = " + age;
        };

        String run = myAction3.run("jim", 12);
        System.out.println("run = " + run);


        Consumer<String> consumer = data -> System.out.println("有参,无返回值 : " + data);
        consumer.accept("Consumer");

        Supplier<String> supplier = () -> "AAA";
        System.out.println("supplier.get() = " + supplier.get());

        Function<String, Integer> function = (data) -> {
            System.out.println("data = " + data);
            return 12;
        };
        System.out.println("function.apply = " + function.apply("jim"));

    }

    @Test
    public void test_33() {

        MyAction33 myAction3 = (name, age) -> {
            System.out.println("有多参,有返回值........");
            return "有参,有返回值 : " + " name = " + name + " age = " + age;
        };

        System.out.println("myAction3 = " + myAction3.run2("tom", 12));
    }

    @Test
    public void test_00() {

        /// 系统提供的 4 个
        Consumer<String> consumer = data -> System.out.println("有参,无返回值 : " + data);
        consumer.accept("Consumer");

        Supplier<String> supplier = () -> "AAA";
        System.out.println("supplier.get() = " + supplier.get());

        Function<String, Integer> function = (data) -> {
            System.out.println("data = " + data);
            return 12;
        };
        System.out.println("function.apply = " + function.apply("jim"));

        Predicate<String> predicate = (data) -> {

            System.out.println("data = " + data);
            return false;
        };
        System.out.println("predicate.test(\"jim\") = " + predicate.test("jim"));


    }

    @Test
    public void test_testFunc() {

        testFunc(10, (data) -> {
            System.out.println("data = " + data);
        });
    }

    void testFunc(int age, Consumer<String> consumer) {

        String rest = "我是..." + age;
        consumer.accept(rest);
    }

    @Test
    public void test_testFuncSupplier() {

        testFuncSupplier(10, () -> {

            return "我是jim";
        });
    }

    void testFuncSupplier(int age, Supplier<String> supplier) {
        String s = supplier.get();
        System.out.println("s = " + s);
    }

    @Test
    public void test_testFuncFunction() {

        testFuncFunction(10, (data) -> {

            System.out.println("data = " + data);
            return "信息 : " + data;
        });
    }

    void testFuncFunction(int age, Function<String, String> function) {


        String rest = "我是..." + age;

        String s = function.apply(rest);
        System.out.println("s = " + s);


    }

    /// 方法引用,构造器引用
    @Test
    void test11() {

        /// 类 调用 静态方法
        Function<String, String> function = LambdaTest::actionFunc;
        System.out.println("function.apply = " + function.apply("jim"));

        // 实例 调用 实例方法
        Function<String, String> function2 = new LambdaTest()::actionFunc2;
        System.out.println("function.apply = " + function2.apply("tom"));

        /// 类 调用 实例方法
        /// <> 这里里面必须是参数
        BiPredicate<LambdaTest, LambdaTest> biPredicate2 = LambdaTest::actionFunc3;

        /// 第一个参数 是 方法 的对象
        MyAction44 myAction44 = LambdaTest::actionFunc4;
        String s44 = myAction44.run(new LambdaTest(), "jim");
        System.out.println("s44 = " + s44);


        BiPredicate<String, String> biPredicate = String::equals;
        System.out.println("biPredicate.test = " + biPredicate.test("A", "a"));
        System.out.println("biPredicate.test = " + biPredicate.test("A", "A"));

        MyAction2 myAction = System.out::println;
        myAction.run("哈哈哈.......");

    }

    String actionFunc2(String data) {
        System.out.println("data = " + data);
        return "aaa";
    }

    boolean actionFunc3(LambdaTest data) {
//        System.out.println("data = " + data + "data2 = "+data2);
        return false;
    }

    String actionFunc4(String data) {
//        System.out.println("data = " + data + "data2 = "+data2);
        return "false";
    }

}
