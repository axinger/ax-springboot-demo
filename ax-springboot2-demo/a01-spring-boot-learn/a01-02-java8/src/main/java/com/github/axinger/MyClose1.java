package com.github.axinger;

import java.io.Closeable;
import java.io.IOException;

public class MyClose1 implements Closeable {

    @Override
    public void close() throws IOException {
        System.out.println("MyClose1----close了");
    }
}
