package com.github.axinger;

import cn.hutool.script.ScriptRuntimeException;
import cn.hutool.script.ScriptUtil;
import org.junit.jupiter.api.Test;

import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class ScriptUtilTests {

    @Test
    void test1() {

        CompiledScript script = ScriptUtil.compile("var a = 1+2");
        try {
            script.eval();
            Object o = script.getEngine().get("a");
            System.out.println("o = " + o);
        } catch (ScriptException e) {
            throw new ScriptRuntimeException(e);
        }


    }

    /// groovy 脚本
    @Test
    void test1_1() throws ScriptException {

        ScriptEngine script = ScriptUtil.getGroovyEngine();
        // 设置变量
        script.put("a", 10);
        script.put("b", 20);
        script.put("c", 20);
        Object eval = script.eval("a + b * c");
        System.out.println("eval = " + eval);
    }

    @Test
    void test2() {

        // 创建ScriptEngineManager对象
        ScriptEngineManager manager = new ScriptEngineManager();

        // 获取JavaScript引擎
        ScriptEngine engine = manager.getEngineByName("nashorn");

        try {
            // 执行JavaScript代码
            engine.eval("print('Script test!');");

            // 获取JavaScript中的变量a的值
//            Object a = engine.get("a");
//
//            // 输出结果
//            System.out.println("a = " + a);  // 输出: a = 3
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }


    @Test
    void test3() {
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try {
            engine.eval("print('Hello, World!');");
            Object result = engine.eval("2 + 2");
            System.out.println("Result: " + result);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }
}
