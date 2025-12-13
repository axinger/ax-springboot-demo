package com.github.axinger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// public interface TestInterface<T> {
public interface TestInterface<T> {
//    private T t;

    void onBack();
}


class BInterface {

    Runnable runnable;

    BInterface(Runnable runnable) {
        this.runnable = runnable;
    }

    public void run() {
        this.runnable.run();
    }
}


class Test {
    public static void main(String[] args) {


        TestInterface<Student> testInterface1 = () -> {

        };

        TestInterface<Student> testInterface = new TestInterface<>() {
            @Override
            public void onBack() {
                // TODO Auto-generated method stub
                System.out.println("this" + this);
                System.out.println("getClass" + getClass());
                System.out.println("" + getClass().getSuperclass());
                System.out.println("" + getClass().getGenericSuperclass());
            }
        };

        testInterface.onBack();
    }
}


class Test2 {
    public static void main(String[] args) {
        BInterface bInterface = new BInterface(() -> System.out.println("1111111111111111111"));
        bInterface.run();
    }
}


class Test3 {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {

        });
    }
}

class Test4 {
    public static void main(String[] args) {

        Map<String, Object> map = new HashMap<>();
        map.put("name", "jim");
        // after JDK1.8
        map.computeIfAbsent("name", key -> {
            System.out.println("key = " + key);
            return key;
        });
        Object age = map.computeIfAbsent("age", key -> {
            System.out.println("key = " + key);
            return 20;
        });
        System.out.println("age = " + age);
        System.out.println("map = " + map);
    }
}
