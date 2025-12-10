package com.github.axinger;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDouble;
import com.googlecode.aviator.runtime.type.AviatorObject;

import java.util.Map;

/**
 * 多参数自定义函数
 */
public class MaxFunction extends AbstractVariadicFunction {

    @Override
    public String getName() {
        return "myMax";
    }

    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("至少需要一个参数");
        }

        double max = Double.MIN_VALUE;
        for (AviatorObject arg : args) {
            Number num = FunctionUtils.getNumberValue(arg, env);
            max = Math.max(max, num.doubleValue());
        }

        return AviatorDouble.valueOf(max);
    }
}
