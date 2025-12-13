package com.github.axinger;

import org.junit.jupiter.api.Test;

import java.io.File;

public class FileTests {

    @Test
    void test() {

        File file = new File("a/b/c/124.png");
        System.out.println("file.getName() = " + file.getName());

        System.out.println("file.getPath() = " + file.getPath());
        System.out.println("file.getAbsolutePath() = " + file.getAbsolutePath());

        System.out.println("file.getParent() = " + file.getParent());
    }
}
