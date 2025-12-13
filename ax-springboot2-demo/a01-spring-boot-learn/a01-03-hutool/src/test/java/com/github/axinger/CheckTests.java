package com.github.axinger;

import org.junit.jupiter.api.Test;

public class CheckTests {


    String mes(String name) {

        int i = 1 / 0;
        return "abc";
    }

    @Test
    void test1() {

        // String string = null;
        //
        // String obj = CheckedUtil.uncheck(CheckTests::mes).call("2");
        // System.out.println("obj = " + obj);


        // System.out.println("string = " + string);
    }

    // 代码中如果遇到一个方法调用声明了受检查异常那么我们的代码就必须这样写

    @Test
    void test2() {
        //     // Map<String, String> describedObject = null;
        //     // try {
        //     //     describe = BeanUtils.describe(new Object());
        //     // } catch (IllegalAccessException e) {
        //     //     throw new RuntimeException(e);
        //     // } catch (
        //     //         InvocationTargetException e) {
        //     //     throw new RuntimeException(e);
        //     // } catch (NoSuchMethodException e) {
        //     //     throw new RuntimeException(e);
        //     // } // use describedObject ...
        //     // // 上面的代码增加了异常块使得代码不那么流畅，现在可以这样写：
        //     Map<String, String> describedObject = CheckedUtil.uncheck(BeanUtils::describe).call(new Object());
        //     // use describedObject ... CheckedUtil.uncheck 方法接受任意可以转化成 cn.hutool.core.lang.func.Func* 函数式接口的 Lambda 表达式。返回对应的函数式对象。
        //     上述代码可以理解为：
        //
        //     Func0<Object, Map<String, String>> aFunc = CheckedUtil.uncheck(BeanUtils::describe);
        //     Map<String, String> describedObject = aFunc.call(传入参数);
        // 该aFunc对象代表的就是BeanUtils::describe这个表达式，且在内部转化了检查类型异常，不需要代码里面显示处理。
    }
}
