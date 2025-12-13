package com.github.axinger.testinterface;

public interface InterfacePerson {

    String name2 = "";

    void mothod4();

    default void mothod5() {
        System.out.println("mothod5==========");
    }

    default void mothod6() {
        System.out.println("mothod6==========");
    }
}


interface People {

    default void eat() {
        System.out.println("人吃饭");
    }
}

interface Man {
    default void eat() {
        System.out.println("男人吃饭");
    }
}


interface Boy extends Man, People {

    @Override
    // 同时实现父类
    default void eat() {
        Man.super.eat();
        People.super.eat();
    }
}


class Student implements Man, People {

    public static void main(String[] args) {
        Student student = new Student();
        student.eat();
    }

    @Override
    public void eat() {
        Man.super.eat();

    }
}
