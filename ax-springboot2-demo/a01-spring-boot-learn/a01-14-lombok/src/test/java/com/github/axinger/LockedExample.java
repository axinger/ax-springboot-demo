package com.github.axinger;

import lombok.Locked;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockedExample {
    public final Lock baseLock = new ReentrantLock();
    private int value = 0;

    @Locked.Read
    public int getValue() {
        return value;
    }

    @Locked.Write
    public void setValue(int newValue) {
        value = newValue;
    }

    @Locked("baseLock")
    public void foo() {
        System.out.println("bar");
    }
}
