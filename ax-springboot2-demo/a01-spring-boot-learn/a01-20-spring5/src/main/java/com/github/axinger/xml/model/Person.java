package com.github.axinger.xml.model;

import java.util.List;

public class Person {

    private String name;
    private String title;
    private String address;

    private List<Dog> dogs;

    private IdCard idCard;

    public Person() {
        System.out.println("1.通过构造器创建bean实例;");
    }

    public void add() {
        System.out.println(" name = " + name + " title = " + title + " address = " + address + " dogs = " + dogs);
    }

    public List<Dog> getDogs() {
        return dogs;
    }

    public void setDogs(List<Dog> dogs) {
        this.dogs = dogs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        System.out.println("2.为bean的属性设置值和对其他bean引用(调用set方法);");
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public IdCard getIdCard() {
        return idCard;
    }

    public void setIdCard(IdCard idCard) {
        this.idCard = idCard;
    }

    /// 创建执行的初始化方法
    public void initMethod() {
        System.out.println("4.调用bean的初始化方法(需要进行配置初始化的方法)");
    }

    public void destroyMethod() {
        System.out.println("7.当容器关闭时候,调用bean的销毁方法(需要进行配置销毁的方法);");
    }


    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", address='" + address + '\'' +
                ", dogs=" + dogs +
                ", idCard=" + idCard +
                '}';
    }
}
