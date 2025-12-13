package com.github.axinger;

import cn.hutool.core.io.resource.ResourceUtil;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyArray;
import org.graalvm.polyglot.proxy.ProxyObject;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JSTests {

    @Test
    public void test1() {
        try (Context context = Context.create()) {
            String jsCode = "function add(a, b) { return a + b; } add(2, 3);";
            int result = context.eval("js", jsCode).asInt();
            System.out.println("结果" + result); // 5
        }
    }


    @Test
    public void test2() {

        // 读取 JS 文件内容（你可以继续用 Hutool 的 ResourceUtil）
        String jsCode = ResourceUtil.readUtf8Str("js/test02.js");

        try (Context context = Context.create("js")) {
            /// 方式1：直接执行脚本返回值 方式1依赖于JS脚本的最后一行返回值
            // 执行整个 JS 脚本，返回最后一个表达式的值（这里是 function 对象）
            Value result = context.eval("js", jsCode);
//            // 确保 result 是一个函数
            if (result.canExecute()) {
                Value returnValue = result.execute(1, 2); // 传入 a=5, b=7
                System.out.println("JS 返回值: " + returnValue.as(Integer.class)); // 输出: 45
            } else {
                System.out.println("JS 文件未正确导出可执行函数");
            }
        } catch (Exception e) {
            System.err.println("执行错误 = " + e);
        }
    }

    @Test
    public void test3() {
        // 方式2：通过绑定对象获取函数
        String jsCode = ResourceUtil.readUtf8Str("js/test03.js");
        try (Context context = Context.create("js")) {
            // 先执行脚本（函数被注册到绑定对象中）
            context.eval("js", jsCode);

            // 通过绑定对象获取特定函数
            Value sumFunction = context.getBindings("js").getMember("sum");
            if (sumFunction != null && sumFunction.canExecute()) {
                Value sumResult = sumFunction.execute(1, 2);
                System.out.println("Java端获取的sum结果: " + sumResult.asInt());
            }

            // 还可以获取其他函数
            Value multiplyFunction = context.getBindings("js").getMember("multiply");
            if (multiplyFunction != null && multiplyFunction.canExecute()) {
                Value multiplyResult = multiplyFunction.execute(3, 4);
                System.out.println("Java端获取的乘法结果: " + multiplyResult.asInt());
            }


            Value add = context.getBindings("js").getMember("add");
            if (add != null && add.canExecute()) {
                // 准备Map参数
                Map<String, Object> params = new HashMap<>();
                params.put("a", 1);
                params.put("b", 2);

                // 将Map转换为ProxyObject以便JavaScript可以访问
                ProxyObject proxyParams = ProxyObject.fromMap(params);

                // 执行JavaScript函数并传递参数
                Value result = add.execute(proxyParams);
                System.out.println("Java端获取的add结果: " + result.asInt());
            }


            Value add2 = context.getBindings("js").getMember("add2");
            if (add2 != null && add2.canExecute()) {
                List<Object> params = new ArrayList<>();
                params.add(11);
                params.add(12);
                ProxyArray proxyArray = ProxyArray.fromList(params);
                // 执行JavaScript函数并传递参数
                Value result = add2.execute(proxyArray);
                System.out.println("Java端获取的add2结果: " + result.asInt());
            }
        }
    }

}
