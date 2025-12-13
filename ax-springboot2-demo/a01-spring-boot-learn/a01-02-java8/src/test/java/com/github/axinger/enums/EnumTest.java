package com.github.axinger.enums;

import cn.hutool.core.lang.func.Func1;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.EnumSet;


public class EnumTest {
    @Test
    public void test1() {


        System.out.println("Week.ONE = " + Week.ONE);
        System.out.println("Week.ONE.name() = " + Week.ONE.name());
        System.out.println("Week.ONE.ordinal() = " + Week.ONE.ordinal());
        System.out.println("Week.valueOf(1) = " + Week.valueOf("ONE"));

        System.out.println("Week.fromCode(1) = " + Week.fromCode(1));
        System.out.println("Week.fromCode(0) = " + Week.fromCode(0));

        System.out.println("Week.from(Week::getDescription,\"1\") = " + Week.from(Week::getName, "1"));

        System.out.println("Week.from(Week::getDescription, \"一\") = " + Week.from(Week::getName, "一"));


        System.out.println("Week.from(Week::getCode, 1) = " + Week.from(Week::getCode, 1));
    }

    @Test
    void test2() {
        OuterEnum.InnerEnum e = OuterEnum.InnerEnum.X;

        System.out.println("e = " + e);
    }

    @Test
    void test3() {
        EnumSet<Week> weeks = EnumSet.allOf(Week.class);
        System.out.println("weeks = " + weeks);
    }

    @Getter
    enum Week {
        NONE("", -1),
        ONE("一", 1),
        TWO("二", 2),

        ;

        private final String name;
        private final Integer code;

        Week(String name, Integer code) {
            this.name = name;
            this.code = code;
        }


        public static Week fromCode(Integer code) {
            return Arrays.stream(Week.values())
                    .filter(val -> val.getCode() == code)
                    .findFirst()
                    .orElse(NONE);
        }

        /**
         * 枚举通用取值
         *
         * @param func
         * @param name
         * @param <R>
         * @return
         */
        public static <R> Week from(Func1<R, ?> func, Object name) {
            String fieldName = LambdaUtil.getFieldName(func);
            return Arrays.stream(Week.values())
                    .filter(val -> ObjectUtil.equals(ReflectUtil.getFieldValue(val, fieldName), name))
                    .findFirst()
                    .orElse(NONE);
        }

    }

    public enum OuterEnum {
        A,
        B,
        C;

        public enum InnerEnum {
            X,
            Y,
            Z;
        }
    }
}

