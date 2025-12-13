package com.github.axinger;

import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.EnumUtil;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EnumUtilTests {
    @Test
    void test1() {
        List<String> names = EnumUtil.getNames(Gender.class);
        System.out.println(names); //[MALE, FEMALE, UNKNOWN]
    }

    @Test
    void test2() {

        // 指定属性的值
        List<Object> types = EnumUtil.getFieldValues(Gender.class, LambdaUtil.getFieldName(Gender::getCode));
        System.out.println("types = " + types); //types = [1, 2, 3]

        List<Object> fieldValues = EnumUtil.getFieldValues(Gender.class, LambdaUtil.getFieldName(Gender::getAlias));
        System.out.println("fieldValues = " + fieldValues); //fieldValues = [男性, 女性, 未知]
    }

    @Test
    void test3() {
        Map<String, Gender> enumMap = EnumUtil.getEnumMap(Gender.class);
        System.out.println("enumMap = " + enumMap); //{MALE=MALE, FEMALE=FEMALE, UNKNOWN=UNKNOWN}

        Gender test1 = enumMap.get("FEMALE");// 结果为：Gender.FEMALE
        System.out.println("test1 = " + test1);
    }

    @Test
    void test4() {
        Map<String, Object> enumMap = EnumUtil.getNameFieldMap(Gender.class, LambdaUtil.getFieldName(Gender::getCode));
        System.out.println("enumMap = " + enumMap); //enumMap = {MALE=1, FEMALE=2, UNKNOWN=3}
        Object test1 = enumMap.get("MALE");// 结果为：type1
        System.out.println("test1 = " + test1);
    }

    @Test
    void test5() {
        Map<String, Object> enumMap = EnumUtil.getNameFieldMap(Gender.class, LambdaUtil.getFieldName(Gender::getAlias));
        System.out.println("enumMap = " + enumMap);


        Object test1 = enumMap.get("MALE");// 结果为：type1
        System.out.println("test1 = " + test1);
        System.out.println("Gender.TEST1.name = " + Gender.MALE.name());
        Object o = enumMap.get(Gender.MALE.name());
        System.out.println("o = " + o);
    }

    @Test
    void test6() {
        List<String> fieldNames = EnumUtil.getFieldNames(Gender.class);
        System.out.println("fieldNames = " + fieldNames); //fieldNames = [code, alias, name]

        LinkedHashMap<String, Gender> enumMap = EnumUtil.getEnumMap(Gender.class);
        System.out.println("enumMap = " + enumMap); //enumMap = {MALE=MALE, FEMALE=FEMALE, UNKNOWN=UNKNOWN}
    }


    @Test
    void test7() {

        System.out.println("Gender.FEMALE.ordinal() = " + Gender.FEMALE.ordinal());

        Gender testEnum = EnumUtil.getBy(Gender::getCode, 12);
        System.out.println("testEnum = " + testEnum);

        Gender testEnum2 = EnumUtil.getBy(Gender::getCode, 15);
        System.out.println("找不到,给个null testEnum2 = " + testEnum2);

        Gender testEnum3 = EnumUtil.getBy(Gender::getCode, 15, Gender.UNKNOWN);
        System.out.println("找不到,给个默认值 testEnum3 = " + testEnum3);


        String fieldBy = EnumUtil.getFieldBy(Gender::getAlias, Gender::getCode, 12);
        System.out.println("fieldBy = " + fieldBy);

        Integer fieldBy2 = EnumUtil.getFieldBy(Gender::getCode, Gender::getCode, 12);
        System.out.println("fieldBy2 = " + fieldBy2);

    }

}
