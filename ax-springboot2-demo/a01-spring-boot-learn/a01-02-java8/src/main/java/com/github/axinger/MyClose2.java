package com.github.axinger;

import java.io.Closeable;
import java.io.IOException;

public class MyClose2 implements Closeable {

    @Override
    public void close() throws IOException {
        System.out.println("MyClose2----close了");
    }
}
