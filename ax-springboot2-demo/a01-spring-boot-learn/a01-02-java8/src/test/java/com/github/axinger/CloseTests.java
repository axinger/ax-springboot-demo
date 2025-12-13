package com.github.axinger;

import lombok.Cleanup;
import org.junit.jupiter.api.Test;

public class CloseTests {

    @Test
    void test01() {
        // 语法糖，不用主动关闭流
        try (MyClose1 myClose1 = new MyClose1();
             MyClose2 myClose2 = new MyClose2();
        ) {
            System.out.println("myClose1 = " + myClose1);
            System.out.println("myClose2 = " + myClose2);

        } catch (Exception e) {
            System.out.println("e = " + e);
        }
    }

    @Test
    void test02() {
        // lombok注解
        try {

            @Cleanup MyClose1 myClose1 = new MyClose1();
            @Cleanup MyClose2 myClose2 = new MyClose2();
            System.out.println("myClose1 = " + myClose1);
            System.out.println("myClose2 = " + myClose2);

        } catch (Exception e) {
            System.out.println("e = " + e);
        } finally {
            System.out.println("finally===============");
        }
    }
}
