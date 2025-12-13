package com.github.axinger;

import cn.hutool.core.lang.func.LambdaUtil;
import org.junit.jupiter.api.Test;

public class LambdaUtilTest {

    @Test
    void test() {
        String fieldName = LambdaUtil.getFieldName(Person::getName);
        System.out.println("fieldName = " + fieldName);

        String methodName = LambdaUtil.getMethodName(Person::getName);
        System.out.println("methodName = " + methodName);
        String methodName1 = LambdaUtil.getMethodName(Person::log1);
        System.out.println("methodName1 = " + methodName1);


        String fieldName1 = LambdaUtil.getFieldName(Person::isSex);
        System.out.println("fieldName1 = " + fieldName1);
    }
}
