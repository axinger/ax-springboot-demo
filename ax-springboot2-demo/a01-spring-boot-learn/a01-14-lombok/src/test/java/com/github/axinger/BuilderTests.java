package com.github.axinger;

import com.github.axinger.model.SingularTests;
import org.junit.Test;

import java.util.ArrayList;

public class BuilderTests {


    @Test
    public void test1() {
        SingularTests build1 = SingularTests.builder()
                .fingers(new ArrayList<>())
                .build();
        System.out.println("build1 = " + build1);

        SingularTests build2 = SingularTests.builder()
//                .finger("小拇指")
//                .finger("食指")
//                .clearFingers()
                .build();
        System.out.println("build2 = " + build2);
//        build2.getFingers().add("1");
//
//        BuilderExample example = new BuilderExample();
//        example.fingers.add("1");
//        System.out.println("example = " + example);
    }
}
