package com.github.axinger.xml.model;

public class User {

    private final String name;
    private final String title;

    private final String address;

    public User(String name, String title, String address) {
        this.name = name;
        this.title = title;
        this.address = address;
    }

    public void add() {
        System.out.println(" name = " + name + " title = " + title + " address = " + address);
    }
}
