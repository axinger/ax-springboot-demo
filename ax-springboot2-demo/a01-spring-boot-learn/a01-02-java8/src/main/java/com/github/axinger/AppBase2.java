package com.github.axinger;


public interface AppBase2 {

    default void start(String name) {
        System.out.println("name = " + name);
        handler(name + "：start传值");
    }

    void handler(String name);

}


class MyApp2 implements AppBase2 {


    public static void main(String[] args) {
        new MyApp2().start("tom");
    }

    @Override
    public void handler(String name) {
        System.out.println("MyApp2 name = " + name);
    }
}
