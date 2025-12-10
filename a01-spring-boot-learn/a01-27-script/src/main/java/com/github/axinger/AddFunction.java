package com.github.axinger;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * 自定义函数 - 实现 AviatorFunction 接口
 */
public class AddFunction extends AbstractFunction {

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {
        Number num1 = FunctionUtils.getNumberValue(arg1, env);
        Number num2 = FunctionUtils.getNumberValue(arg2, env);
        return AviatorDouble.valueOf(num1.doubleValue() + num2.doubleValue());
    }
}
