package com.github.axinger;


public abstract class AppBase {

    public void start(String name) {

        System.out.println("name = " + name);

        handler(name);
    }

    public abstract void handler(String name);

}

class MyApp extends AppBase {

    public static void main(String[] args) {
        new MyApp().start("jim");

    }

    @Override
    public void handler(String name) {
        System.out.println("MyApp" + name);
    }
}
