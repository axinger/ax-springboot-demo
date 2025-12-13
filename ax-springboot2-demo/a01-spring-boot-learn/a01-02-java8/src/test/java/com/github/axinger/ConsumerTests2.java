package com.github.axinger;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author xing
 * @version 1.0.0
 * @ClassName ConsumerTests2.java
 * @Description TODO
 * @createTime 2022年03月15日 13:11:00
 */


class ConsumerTests2 {
    //【2】Predicate和Consumer的使用：student如果在某个条件predicate.test()成立，就执行consumer.accept()
    public static void updateStudentFee(Student student, Predicate<Student> predicate, Consumer<Student> consumer) {
        if (predicate.test(student))
            consumer.accept(student);
    }

    public static void main(String[] args) {
        //【测试1】传统的书写方式
        Predicate<Student> p = new MyPredicate();
        Consumer<Student> c = new MyConsumer();
        Student student1 = new Student("Rajat", 8.5);
        updateStudentFee(student1, p, c);
        student1.printFee();

        //【测试2】lambada表达方式
        Student student2 = new Student("Ashok", 9.5);
        updateStudentFee(student2, student -> student.grade > 8.5, student -> student.feeDiscount = 30.0);
        student2.printFee();

        //【测试3】Method References方式，具体可参见 Filter（2）：处理异步请求#再看一个小例子 中的说明
        Student student3 = new Student("Test", 9.0);
        ConsumerTests2 test = new ConsumerTests2();
        updateStudentFee(student3, p, test::mySetDiscount);
        student3.printFee();
    }

    //（方式3）方法实现费率设置的处理
    public void mySetDiscount(Student student) {
        student.feeDiscount = 10.0;
    }

    // 【1】学费类: Student -》 提供简单的根据成绩计算费用的类。
    public static class Student {
        String name;
        Double grade;
        Double feeDiscount = 0.0;
        Double baseFee = 20000.0;

        public Student(String name, Double grade) {
            this.name = name;
            this.grade = grade;
        }

        public void printFee() {
            Double fee = baseFee - ((baseFee * feeDiscount) / 100);
            System.out.println(name + "'s fee after discount(" + feeDiscount + "): " + fee);
        }
    }

    //（方式1）实现Predicate和Consumer，这两个接口也比较简单
    //（方式1.1）实现Predicate接口，具体需要实现条件判断test()方法
    public static class MyPredicate implements Predicate<Student> {
        @Override
        public boolean test(Student t) {
            return t.grade >= 8.0;
        }
    }

    //（方式1.2）实现Consumer接口，具体需要实现接纳的处理accept()方法
    public static class MyConsumer implements Consumer<Student> {
        @Override
        public void accept(Student t) {
            t.feeDiscount = 50.0;
        }
    }
}
